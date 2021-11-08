package com.app.APICode.notification;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.app.APICode.ctest.CtestService;
import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    private RestaurantService restaurants;
    private EmployeeService employees;
    private UserService users;
    private CtestService ctests;

    private static final int CTEST_NUM_ELAPSED_DAYS = 4;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notifications, RestaurantService restaurants,
            EmployeeService employees, UserService users, @Lazy CtestService ctests) {
        this.notifications = notifications;
        this.restaurants = restaurants;
        this.employees = employees;
        this.users = users;
        this.ctests = ctests;
    }

    @Override
    public Notification addNewEmployeeApprovalNotification(String username, Long restaurantId) {
        Employee pendingEmployee = employees.getEmployeeByUsername(username);
        User owner = restaurants.getRestaurantOwner(restaurantId);
        String notificationText = String.format(
                "You have a pending employee request from %s %s. Please review it under your Employee List.",
                pendingEmployee.getUser().getFirstName(), pendingEmployee.getUser().getLastName());

        Notification newNotification = new Notification(notificationText, owner);
        return notifications.save(newNotification);
    }

    /**
     * Cron task to automatically check for employees and users who need to do their
     * tests When the number of days <= 3, a new notification will be created at
     * midnight. The days and cron schedule is customisable from the properties file
     * 
     * @return int number of notifications generated
     */
    @Override
    @Scheduled(cron = "${notification.cron.ctest}")
    public int generateCtestNotification() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        users.getAllUsers().parallelStream().forEach(u -> {
            if (checkAndGenerateNotifications(u)) {
                atomicInteger.getAndIncrement();
            }
        });

        return atomicInteger.get();
    }

    /**
     * Check if the last test done is more than the predefined limit for
     * notifications. Returns true if yes and a notification is created. Else,
     * returns false.
     * 
     * @param user
     * @return
     */
    public boolean checkAndGenerateNotifications(User user) {
        Instant nextCtestDate = ctests.getNextCtestByUsername(user.getUsername()).toInstant();
        Instant notificationDateThreshold = ZonedDateTime.now().minusDays(CTEST_NUM_ELAPSED_DAYS).toInstant();

        if (notificationDateThreshold.isAfter(nextCtestDate)) {
            String notificationText = "You need to complete a Covid Test by: " + nextCtestDate;
            Notification newNotification = new Notification(notificationText, user);
            notifications.save(newNotification);
            return true;
        }

        return false;
    }

    /**
     * This function will fetch all the notification of a user and filter them to
     * only display the ones which are not read yet
     */
    @Override
    public List<Notification> getNotificationsByUsername(String username) {
        User user = users.getUserByUsername(username);
        return filterUnreadNotifications(user.getNotifications());
    }

    /**
     * Internal function to filter the notifications to only return unread ones
     * 
     * @param notificationsList
     * @return List<Notification>
     */
    public List<Notification> filterUnreadNotifications(List<Notification> notifications) {
        return notifications.stream().filter(n -> !n.isSeen()).collect(Collectors.toList());
    }

    /**
     * Function which will change all of the notification of a user to a state of
     * read: true
     * 
     * @param username the username of the user
     */
    @Override
    public List<Notification> markAllNotificationsRead(String username) {
        User user = users.getUserByUsername(username);
        notifications.updateAllNotificationToRead(user.getId());
        return user.getNotifications();
    }

    /**
     * Function which will only mark a single notification as read, using the
     * notification ID. It will also ensure the notification belongs to the user.
     * 
     * @param username username of user
     * @param id       notification ID
     */
    @Override
    public Notification markSingleNotificationRead(String username, Long notifId) {
        User user = users.getUserByUsername(username);
        return notifications.findByIdAndUser(notifId, user.getId()).map(notifs -> {
            notifs.setSeen(true);
            return notifications.save(notifs);
        }).orElseThrow(() -> new NotificationNotFoundException());
    }

}

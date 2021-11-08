package com.app.APICode.notification;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.app.APICode.ctest.CtestService;
import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    private RestaurantService restaurants;
    private EmployeeService employees;
    private UserService users;
    private CtestService ctests;

    private static final int CTEST_NUM_ELAPSED_DAYS = 3;
    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notifications, RestaurantService restaurants,
            UserService users, EmployeeService employees, CtestService ctests) {
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
        Date nextCtestDate = ctests.getNextCtestByUsername(user.getUsername());
        Date notificationDateThreshold = new Date(System.currentTimeMillis() + CTEST_NUM_ELAPSED_DAYS * DAY_IN_MS);

        if (notificationDateThreshold.compareTo(nextCtestDate) >= 0) {
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
    @Transactional
    @Override
    public List<Notification> markAllNotificationsRead(String username) {
        User user = users.getUserByUsername(username);
        notifications.updateAllNotificationToRead(user);
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
        return notifications.findByIdAndUser(notifId, user).map(notifs -> {
            notifs.setSeen(true);
            return notifications.save(notifs);
        }).orElseThrow(() -> new NotificationNotFoundException());
    }

}

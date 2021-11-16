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

/**
 * Implementation of the {@link Notification} service layer
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    private RestaurantService restaurants;
    private EmployeeService employees;
    private UserService users;
    private CtestService ctests;

    private static final int CTEST_NUM_ELAPSED_DAYS = 3;
    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    public NotificationServiceImpl() {
    }

    @Autowired
    public void setNotifications(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Autowired
    public void setRestaurants(RestaurantService restaurants) {
        this.restaurants = restaurants;
    }

    @Autowired
    public void setEmployees(EmployeeService employees) {
        this.employees = employees;
    }

    @Autowired
    public void setUsers(UserService users) {
        this.users = users;
    }

    @Autowired
    public void setCtests(CtestService ctests) {
        this.ctests = ctests;
    }

    @Override
    public Notification addNewEmployeeApprovalNotification(String username, Long restaurantId) {
        Employee pendingEmployee = employees.getEmployeeByUsername(username);
        User owner = restaurants.getRestaurantOwner(restaurantId);
        String notificationText = String.format(
                "You have a pending employee request from %s %s. Please review it under your Employee List.",
                pendingEmployee.getUser().getFirstName(), pendingEmployee.getUser().getLastName());

        return addNewNotification(notificationText, owner);
    }

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

    @Override
    public boolean checkAndGenerateNotifications(User user) {
        if (user.getEmployee() == null) {
            return false;
        }

        Date nextCtestDate = ctests.getNextCtestByUsername(user.getUsername());
        Date notificationDateThreshold = new Date(System.currentTimeMillis() + CTEST_NUM_ELAPSED_DAYS * DAY_IN_MS);

        if (notificationDateThreshold.compareTo(nextCtestDate) >= 0) {
            String notificationText = "You need to complete a Covid Test by: " + nextCtestDate;
            addNewNotification(notificationText, user);
            return true;
        }

        return false;
    }

    @Override
    public Notification addNewNotification(String notificationText, User user) {
        Notification newNotification = new Notification(notificationText, user);
        return notifications.save(newNotification);
    }

    @Override
    public List<Notification> getNotificationsByUsername(String username) {
        User user = users.getUserByUsername(username);
        return filterUnreadNotifications(user.getNotifications());
    }

    @Override
    public List<Notification> filterUnreadNotifications(List<Notification> notifications) {
        return notifications.stream().filter(n -> !n.isSeen()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<Notification> markAllNotificationsRead(String username) {
        User user = users.getUserByUsername(username);
        notifications.updateAllNotificationToRead(user);
        return user.getNotifications();
    }

    @Override
    public Notification markSingleNotificationRead(String username, Long notifId) {
        User user = users.getUserByUsername(username);
        return notifications.findByIdAndUser(notifId, user).map(notifs -> {
            notifs.setSeen(true);
            return notifications.save(notifs);
        }).orElseThrow(() -> new NotificationNotFoundException());
    }

}

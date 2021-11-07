package com.app.APICode.notification;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.app.APICode.ctest.CtestService;
import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    private RestaurantService restaurants;
    private EmployeeService employees;
    private UserService users;
    private CtestService ctests;

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
    public Notification addNewEmployeeApprovalNotification(String username, Long restaurantId, String designation) {
        Employee pendingEmployee = employees.getEmployeeByUsername(username);
        User owner = restaurants.getRestaurantOwner(restaurantId);
        String notificationText = String.format(
                "You have a pending employee request from %s %s. Please review it under your Employee List.",
                pendingEmployee.getUser().getFirstName(), pendingEmployee.getUser().getLastName());

        Notification newNotification = new Notification(notificationText, owner);
        System.out.println("New notification succesfully created");
        return notifications.save(newNotification);
    }

    @Override
    public Notification upcomingCtestNotification(String username) {
        // TODO Auto-generated method stu
        /*
         * First we have to get the ctest information getNextCtestByUsername then we run
         * getNotificationsByUsername to get the list we run through the list see if
         * there has been any notifications created today if yes then we just return if
         * no then we have to create a new notification with todays date and number of
         * days left
         * 
         */
        User user = users.getUserByUsername(username);
        Date nextCtestDate = ctests.getNextCtestByUsername(username);
        LocalDate today = LocalDate.now();
        List<Notification> userNotis = getNotificationsByUsername(username);
        for (Notification current : userNotis) {
            if (current.getDate().equals(today)) {
                return null;
            }
        }
        String notificationText = "You need to complete a Covid Test by: " + nextCtestDate;
        Notification newNotification = new Notification(notificationText, user);
        System.out.println("New notification succesfully created");
        return notifications.save(newNotification);
        // return null;
    }

    @Override
    public List<Notification> getNotificationsByUsername(String username) {
        User user = users.getUserByUsername(username);
        return user.getNotifications();
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
    public void markSingleNotificationRead(String username, Long id) {
        User user = users.getUserByUsername(username);
        notifications.findByIdAndUser(id, user.getId()).map(notifs -> {
            notifs.setSeen(true);
            return notifications.save(notifs);
        }).orElseThrow(() -> new NotificationNotFoundException());
    }

}

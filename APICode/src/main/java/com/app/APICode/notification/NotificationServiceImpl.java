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

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    private RestaurantService restaurants;
    private EmployeeService employees;
    private UserService users;
    private CtestService ctests;

    public NotificationServiceImpl(NotificationRepository notifications, RestaurantService restaurants, EmployeeService employees, UserService users, CtestService ctests) {
        this.notifications = notifications;
        this.restaurants = restaurants;
        this.employees = employees;
        this.users  = users;
        this.ctests = ctests;
    }

    @Override
    public Notification addNewEmployeeApprovalNotification(String username, Long restaurantId, String designation) {
        Employee pendingEmployee = employees.getEmployeeByUsername(username);
        User owner = restaurants.getRestaurantOwner(restaurantId);
        String notificationText = "You have a pending employee request from " + pendingEmployee.getUser().getFirstName() + " "
        + pendingEmployee.getUser().getLastName() + ". Please review it under your Employee List.";

        Notification newNotification = new Notification(notificationText, owner);
        System.out.println("New notification succesfully created");
        return notifications.save(newNotification);
    }

    @Override
    public Notification upcomingCtestNotification(String username) {
        // TODO Auto-generated method stu
        /*
            First we have to get the ctest information
            getNextCtestByUsername
            then we run getNotificationsByUsername to get the list
            we run through the list see if there has been any notifications created today
            if yes then we just return 
            if no then we have to create a new notification with todays date and number of days left

        */
        User user = users.getUserByUsername(username);
        Date nextCtestDate = ctests.getNextCtestByUsername(username);
        LocalDate today = LocalDate.now();
        List<Notification> userNotis = getNotificationsByUsername(username);
        for(Notification current: userNotis){
            if(current.getDate().equals(today)){
                return null;
            }
        }
        String notificationText = "You need to complete a Covid Test by: "+ nextCtestDate;
        Notification newNotification = new Notification(notificationText, user);
        System.out.println("New notification succesfully created");
        return notifications.save(newNotification);
    }

    @Override
    public List<Notification> getNotificationsByUsername(String username){
        User user = users.getUserByUsername(username);
        return user.getNotifications();
    }


}

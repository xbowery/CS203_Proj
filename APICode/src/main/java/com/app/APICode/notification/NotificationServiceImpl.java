package com.app.APICode.notification;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    
    private RestaurantService restaurants;

    private EmployeeService employees;

    public NotificationServiceImpl(NotificationRepository notifications, RestaurantService restaurants, EmployeeService employees) {
        this.notifications = notifications;
        this.restaurants = restaurants;
        this.employees = employees;
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
    public Notification upcomingCtestNotification(Long user_id) {
        // TODO Auto-generated method stu
        return null;
    }



}

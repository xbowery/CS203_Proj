package com.app.APICode.notification;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeRepository;
import com.app.APICode.restaurant.RestaurantRepository;

import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notifications;
    
    private RestaurantRepository restaurants;

    private EmployeeRepository employees;

    public NotificationServiceImpl(NotificationRepository notifications, RestaurantRepository restaurants, EmployeeRepository employees) {
        this.notifications = notifications;
        this.restaurants = restaurants;
        this.employees = employees;
    }

    @Override
    public Notification addNewEmployeeApprovalNotification(Long userId, Long restaurantId, String designation) {
        // TODO Auto-generated method stub
        Employee pendingEmployee = employees.findById(userId).orElse(null);

        

        String notificationText = "You have a pending employee request from " + pendingEmployee.getUser().getFirstName() + " "
         + pendingEmployee.getUser().getLastName() + ". Please review it under your Employee List.";
        return null;
    }

    @Override
    public Notification upcomingCtestNotification(Long user_id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Notification updateUserRoleNotification(Long user_id) {
        // TODO Auto-generated method stub
        return null;
    }

    
    

}

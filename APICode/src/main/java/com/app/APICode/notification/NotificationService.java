package com.app.APICode.notification;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId, String designation);

    Notification upcomingCtestNotification(Long user_id);

}

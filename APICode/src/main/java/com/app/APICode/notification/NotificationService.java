package com.app.APICode.notification;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(Long userId, Long restaurantId, String designation);

    Notification upcomingCtestNotification(Long user_id);

    Notification updateUserRoleNotification(Long user_id);
}

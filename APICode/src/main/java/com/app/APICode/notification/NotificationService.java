package com.app.APICode.notification;

import java.util.List;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId, String designation);

    Notification upcomingCtestNotification(Long user_id);

    List<Notification> getNotificationsByUsername(String username);
}

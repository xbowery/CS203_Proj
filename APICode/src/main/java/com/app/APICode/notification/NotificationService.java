package com.app.APICode.notification;

import java.util.List;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId);

    Notification upcomingCtestNotification(String username);

    List<Notification> getNotificationsByUsername(String username);

    List<Notification> markAllNotificationsRead(String username);

    Notification markSingleNotificationRead(String username, Long id);
}

package com.app.APICode.notification;

import java.util.List;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId, String designation);

    Notification upcomingCtestNotification(String username);

    List<Notification> getNotificationsByUsername(String username);

    void markAllNotificationsRead(String username);

    void markSingleNotificationRead(String username, Long id);
}

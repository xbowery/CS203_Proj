package com.app.APICode.notification;

import java.util.List;

import com.app.APICode.user.User;

public interface NotificationService {
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId);

    int generateCtestNotification();

    boolean checkAndGenerateNotifications(User user);

    List<Notification> getNotificationsByUsername(String username);

    List<Notification> markAllNotificationsRead(String username);

    Notification markSingleNotificationRead(String username, Long id);

    List<Notification> filterUnreadNotifications(List<Notification> notifications);

    Notification addNewNotification(String notificationText, User user);

}

package com.app.APICode.notification;

import java.util.List;

import com.app.APICode.user.User;

/**
 * Service interface layer that performs CRUD operations for {@link Notification}
 */
public interface NotificationService {
    /**
     * Creates a new notification with the given username and restaurant id.
     * 
     * @param username username of the employee
     * @param restaurantId
     * @return the newly added {@link Notification} object
     */
    Notification addNewEmployeeApprovalNotification(String username, Long restaurantId);

    /**
     * Cron task to automatically check for employees and users who need to do their
     * tests. When the number of days is less than or equal to 3, a new notification will be created at
     * midnight. The days and cron schedule is customisable from the properties file
     * 
     * @return number of notifications generated (integer)
     */
    int generateCtestNotification();

    /**
     * Check if the last test done is more than the predefined limit for
     * notifications. Returns true if yes and a notification is created. Else,
     * returns false.
     * 
     * @param user
     * @return a boolean
     */
    boolean checkAndGenerateNotifications(User user);

    /**
     * This function will fetch all the notification of a user and filter them to
     * only display the ones which are not read yet.
     * 
     * @param username a String containing User's username
     * @return a list of {@link Notification}
     */
    List<Notification> getNotificationsByUsername(String username);

    /**
     * Function which will change all of the notification of a user to a state of
     * read: true
     * 
     * @param username the username of the user * 
     * @return a list of {@link Notification}
     * 
     */
    List<Notification> markAllNotificationsRead(String username);

    /**
     * Function which will only mark a single notification as read, using the
     * notification ID. It will also ensure the notification belongs to the user.
     * <p> If no Notification is found, throw a {@link NotificationNotFoundException}.
     * 
     * @param username username of user
     * @param id       notification ID
     * @return a {@link Notification}
     */
    Notification markSingleNotificationRead(String username, Long id);

    /**
     * Internal function to filter the notifications to only return unread ones.
     * 
     * @param notifications a list of {@link Notification}
     * @return a list of {@link Notification}
     */
    List<Notification> filterUnreadNotifications(List<Notification> notifications);

    /**
     * Saves a new notification with the given string for the given user.
     * 
     * @param notificationText notification details
     * @param user             the user to save as
     * @return Notification the notification saved
     */
    Notification addNewNotification(String notificationText, User user);

}

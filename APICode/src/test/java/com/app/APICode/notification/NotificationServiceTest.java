package com.app.APICode.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;
import com.app.APICode.ctest.CtestService;
import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notifications;

    @Mock
    private EmployeeService employees;

    @Mock
    private RestaurantService restaurants;

    @Mock
    private UserService users;

    @Mock
    private CtestService ctests;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    /**
     * Test and mock the instance when a new employee request will be logged to the
     * business owner's notification. A hardcoded restaurant ID of 1L will be used.
     */
    @Test
    void addEmployeeSignUpNotification_NewEmployee_ReturnsNotification() {
        User employeeUser = new User("employee5@test.com", "employee1", "employee", "1", "testing12345", false,
                "ROLE_EMPLOYEE");
        employeeUser.setEnabled(true);

        Employee employee = new Employee(employeeUser, "Employee");
        Long id = 1L;
        Notification mockNotification = new Notification("New notification!", employeeUser);

        when(employees.getEmployeeByUsername(any(String.class))).thenReturn(employee);
        when(restaurants.getRestaurantOwner(anyLong())).thenReturn(employeeUser);
        when(notifications.save(any(Notification.class))).thenReturn(mockNotification);

        Notification newNotification = notificationService
                .addNewEmployeeApprovalNotification(employee.getUser().getUsername(), id);

        assertNotNull(newNotification);

        verify(employees).getEmployeeByUsername(employee.getUser().getUsername());
        verify(restaurants).getRestaurantOwner(id);
        verify(notifications).save(any(Notification.class));
    }

    /**
     * Test retrieving all notifications of a particular user and verifying the
     * length and content is correct
     */
    @Test
    void getAllUserNotification_ReturnsListOfNotifications() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotification = new Notification("New notification", user);
        user.addNotification(mockNotification);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        List<Notification> allNotifications = notificationService.getNotificationsByUsername(user.getUsername());

        assertEquals(allNotifications.size(), 1);
        assertEquals(allNotifications.get(0).getText(), mockNotification.getText());
        assertEquals(allNotifications.get(0).getUser().getUsername(), mockNotification.getUser().getUsername());

        verify(users).getUserByUsername(user.getUsername());
    }

    @Test
    void markAllUserNotificationRead_ReturnListOfNotifications() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotification = new Notification("New notification", user);
        user.addNotification(mockNotification);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        mockNotification.setSeen(true);

        List<Notification> allNotifications = notificationService.markAllNotificationsRead(user.getUsername());

        assertEquals(allNotifications.size(), 1);
        assertTrue(allNotifications.get(0).isSeen());

        verify(users).getUserByUsername(user.getUsername());
        verify(notifications).updateAllNotificationToRead(user);
    }

    @Test
    void markSingleNotificationRead_ValidID_Success() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotification = new Notification("New notification", user);
        user.addNotification(mockNotification);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        // Mock the ID
        Long id = 1L;
        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(mockNotification, "id", id);

        when(notifications.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(mockNotification));
        when(notifications.save(any(Notification.class))).thenReturn(mockNotification);

        mockNotification.setSeen(true);

        Notification updatedNotification = notificationService.markSingleNotificationRead(user.getUsername(),
                mockNotification.getId());

        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isSeen());

        verify(users).getUserByUsername(user.getUsername());
        verify(notifications).findByIdAndUser(mockNotification.getId(), user);
        verify(notifications).save(mockNotification);

    }

    @Test
    void markSingleNotificationRead_InvalidID_ThrowsException() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotification = new Notification("New notification", user);
        user.addNotification(mockNotification);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        // Mock the ID
        Long id = 1L;
        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(mockNotification, "id", id);

        when(notifications.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        mockNotification.setSeen(true);

        NotificationNotFoundException notFoundException = assertThrows(NotificationNotFoundException.class, () -> {
            notificationService.markSingleNotificationRead(user.getUsername(), mockNotification.getId());
        });

        assertEquals(notFoundException.getMessage(), "Could not find notification.");

        verify(users).getUserByUsername(user.getUsername());
        verify(notifications).findByIdAndUser(mockNotification.getId(), user);
        verify(notifications, never()).save(mockNotification);

    }

    /**
     * Ensure that once all notifications are read, the user should not see them
     * again.
     */
    @Test
    void getAllUserNotification_AllRead_ReturnsEmptyList() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotification = new Notification("New notification", user);
        mockNotification.setSeen(true);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        List<Notification> allNotifications = notificationService.getNotificationsByUsername(user.getUsername());

        assertEquals(allNotifications.size(), 0);

        verify(users).getUserByUsername(user.getUsername());
    }

    @Test
    void filterUnreadNotifications_MixOfReadAndUnread_ReturnsListOfUnread() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Notification mockNotificationRead = new Notification("Read notification", user);
        mockNotificationRead.setSeen(true);

        Notification mockNotificationUnRead = new Notification("Unread notification", user);

        List<Notification> allNotifications = new ArrayList<>();
        allNotifications.add(mockNotificationRead);
        allNotifications.add(mockNotificationUnRead);

        List<Notification> filteredNotifications = notificationService.filterUnreadNotifications(allNotifications);

        assertEquals(filteredNotifications.size(), 1);
        assertEquals(filteredNotifications.get(0).getText(), mockNotificationUnRead.getText());
        assertFalse(filteredNotifications.get(0).isSeen());

    }

    /**
     * We will mock a fake testing date that is supposedly in 3 days.
     */
    @Test
    void checkAndGenerateNotification_PassedThreshold_ReturnsTrue() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Date expiredDate = new Date(System.currentTimeMillis() + (3 * DAY_IN_MS));

        when(ctests.getNextCtestByUsername(anyString())).thenReturn(expiredDate);
        when(notifications.save(any(Notification.class))).thenReturn(null);

        boolean isNotificationCreated = notificationService.checkAndGenerateNotifications(user);

        assertTrue(isNotificationCreated);
        verify(ctests).getNextCtestByUsername(user.getUsername());
        verify(notifications).save(any(Notification.class));
    }

    /**
     * We will mock a fake testing date that is supposedly in 4 days. This will
     * return false.
     */
    @Test
    void checkAndGenerateNotification_BeforeThreshold_ReturnsFalse() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        Date expiredDate = new Date(System.currentTimeMillis() + (4 * DAY_IN_MS));

        when(ctests.getNextCtestByUsername(anyString())).thenReturn(expiredDate);

        boolean isNotificationCreated = notificationService.checkAndGenerateNotifications(user);

        assertFalse(isNotificationCreated);
        verify(ctests).getNextCtestByUsername(user.getUsername());
        verify(notifications, never()).save(any(Notification.class));

    }

    /**
     * Mock 2 users who have their tests due. The expected result will be 2.
     */
    @Test
    void generateCtestNotification_RunsNormally_ReturnsInt() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        User user2 = new User("user2@test.com", "user2", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user2.setEnabled(true);

        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        usersList.add(user2);

        NotificationService spiedService = spy(notificationService);

        when(users.getAllUsers()).thenReturn(usersList);
        doReturn(true).when(spiedService).checkAndGenerateNotifications(any(User.class));

        int numNotificationsCreated = spiedService.generateCtestNotification();

        assertEquals(numNotificationsCreated, 2);

        verify(users).getAllUsers();
        verify(spiedService).checkAndGenerateNotifications(user);
        verify(spiedService).checkAndGenerateNotifications(user2);
    }

    /**
     * Mock 2 users of which only one have their tests due. The expected result will
     * be 1.
     */
    @Test
    void generateAnotherCtestNotification_RunsNormally_ReturnsInt() {
        User user = new User("user@test.com", "user", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user.setEnabled(true);

        User user2 = new User("user2@test.com", "user2", "user", "mock", "testing123", false, "ROLE_EMPLOYEE");
        user2.setEnabled(true);

        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        usersList.add(user2);

        NotificationService spiedService = spy(notificationService);

        when(users.getAllUsers()).thenReturn(usersList);
        doReturn(true).when(spiedService).checkAndGenerateNotifications(user);
        doReturn(false).when(spiedService).checkAndGenerateNotifications(user2);

        int numNotificationsCreated = spiedService.generateCtestNotification();

        assertEquals(numNotificationsCreated, 1);

        verify(users).getAllUsers();
        verify(spiedService).checkAndGenerateNotifications(user);
        verify(spiedService).checkAndGenerateNotifications(user2);
    }
}

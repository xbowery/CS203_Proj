package com.app.APICode.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
        verify(notifications).updateAllNotificationToRead(user.getId());
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

        when(notifications.findByIdAndUser(anyLong(), anyLong())).thenReturn(Optional.of(mockNotification));
        when(notifications.save(any(Notification.class))).thenReturn(mockNotification);

        mockNotification.setSeen(true);

        Notification updatedNotification = notificationService.markSingleNotificationRead(user.getUsername(),
                mockNotification.getId());

        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isSeen());

        verify(users).getUserByUsername(user.getUsername());
        verify(notifications).findByIdAndUser(mockNotification.getId(), user.getId());
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

        when(notifications.findByIdAndUser(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockNotification.setSeen(true);

        NotificationNotFoundException notFoundException = assertThrows(NotificationNotFoundException.class, () -> {
            notificationService.markSingleNotificationRead(user.getUsername(), mockNotification.getId());
        });

        assertEquals(notFoundException.getMessage(), "Could not find notification.");

        verify(users).getUserByUsername(user.getUsername());
        verify(notifications).findByIdAndUser(mockNotification.getId(), user.getId());
        verify(notifications, never()).save(mockNotification);

    }
}

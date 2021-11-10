package com.app.APICode.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.APICode.ctest.CtestRepository;
import com.app.APICode.notification.NotificationService;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.restaurant.RestaurantServiceImpl;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;
import com.app.APICode.user.UserRepository;
import com.app.APICode.user.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private UserService users;

    @Mock
    private RestaurantService restaurants;

    @Mock
    private NotificationService notifications;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getEmployees_ValidBusinessOwnerUsername_ReturnEmployees() {
        // Arrange
        employeeService.setRestaurants(restaurants);
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        List<Employee> employeelist = new ArrayList<>();

        User owner = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_BUSINESS");
        Employee employee = new Employee(owner, "Business Owner");
        employee.setRestaurant(restaurant);
        owner.setEmployee(employee);

        User user2 = new User("pendingemployee2@test.com", "user6", "User", "six", "", false, "ROLE_EMPLOYEE");
        Employee employee2 = new Employee(user2, "HR Manager");
        employee2.setRestaurant(restaurant);
        user2.setEmployee(employee2);

        employeelist.add(employee);
        employeelist.add(employee2);
        restaurant.setEmployees(employeelist);

        when(users.getUserByUsername(anyString())).thenReturn(owner);

        // Act
        List<UserDTO> employeeListByOwner = employeeService.getAllEmployeesByBusinessOwner(owner.getUsername());

        // Assert
        assertNotNull(employeeListByOwner);
        assertEquals(2, employeeListByOwner.size());
        verify(users).getUserByUsername(owner.getUsername());
    }

    @Test
    void getEmployee_ValidUsername_ReturnEmployee() {
        // Arrange
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        user.setEmployee(employee);

        when(users.getUserByUsername(user.getUsername())).thenReturn(user);

        // Act
        Employee savedEmployee = employeeService.getEmployeeByUsername(user.getUsername());

        // Assert
        assertNotNull(savedEmployee);
        verify(users).getUserByUsername(user.getUsername());
    }

    @Test
    void getEmployee_InvalidUsername_ReturnException() {
        // Arrange
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");

        when(users.getUserByUsername(user.getUsername())).thenReturn(user);

        // Act
        EmployeeNotFoundException notFoundException = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeByUsername(user.getUsername());
        });

        // Assert
        assertEquals(notFoundException.getMessage(),
                "Could not find Employee with user Username: " + user.getUsername());
        verify(users).getUserByUsername(user.getUsername());

    }

    @Test
    void getEmployeeDetails_BusinessOwnerRequester_ReturnEmployee() {
        // Arrange
        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);

        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "", false, "ROLE_USER");
        Employee owner = new Employee(user, "Business Owner");
        owner.setRestaurant(testRestaurant);
        user.setEmployee(owner);
        user.setAuthorities("ROLE_BUSINESS");

        User user1 = new User("pendingemployee2@test.com", "user7", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user1, "HR Manager");
        employee.setRestaurant(testRestaurant);
        user1.setEmployee(employee);
        user1.setAuthorities("ROLE_EMPLOYEE");

        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        // Act
        Employee savedEmployee = employeeService.getEmployeeDetailsByUsername(user.getUsername(), user1.getUsername());

        // Assert
        assertNotNull(savedEmployee);
        verify(users).getUserByUsername(user.getUsername());
        verify(users).getUserByUsername(user1.getUsername());

    }

    @Test
    public void addEmployee_NewUsername_ReturnSavedEmployee() {
        // Arrange
        employeeService.setRestaurants(restaurants);
        employeeService.setNotifications(notifications);

        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);

        long businessId = restaurant.getId();

        when(users.save(any(User.class))).thenReturn(user);
        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(restaurants.getRestaurantById(any(Long.class))).thenReturn(restaurant);
        when(notifications.addNewEmployeeApprovalNotification(any(String.class), any(Long.class), any(String.class)))
                .thenReturn(null);
        // Act
        Employee savedEmployee = employeeService.addEmployeeToBusiness(user.getUsername(), "Manager", businessId);

        // Assert
        assertNotNull(savedEmployee);
        verify(users).save(user);
    }

    @Test
    public void addEmployee_InvalidRestaurantId_ReturnNull() {
        // Arrange
        employeeService.setRestaurants(restaurants);
        employeeService.setNotifications(notifications);

        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        long businessId = 10L;

        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(restaurants.getRestaurantById(any(Long.class))).thenReturn(null);

        // Act
        RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
            employeeService.addEmployeeToBusiness(user.getUsername(), "Manager", businessId);
        });

        // Assert
        assertEquals(notFoundException.getMessage(), "Could not find restaurant with ID: 10");
        verify(users).getUserByUsername(user.getUsername());
    }

    @Test
    public void approveEmployee_validUsername_ReturnEmployeeRole() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        employee.setRestaurant(restaurant);
        employee.setStatus("Pending");
        user.setEmployee(employee);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(users.save(any(User.class))).thenReturn(user);

        // Act
        Employee approvedEmployee = employeeService.approveEmployee(user.getUsername());

        // Assert
        assertNotNull(approvedEmployee);
        assertEquals("Active", approvedEmployee.getStatus());

    }

    @Test
    public void deleteEmployee_ValidUsername_ReturnNull() {
        // Arrange
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        when(users.save(any(User.class))).thenReturn(user);
        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        // Act
        employeeService.deleteEmployee(user.getUsername());
        Employee deletedEmployee = user.getEmployee();

        // Assert
        assertNull(deletedEmployee);
        verify(users).save(user);
    }

    @Test
    public void deleteEmployee_InvalidUsername_ReturnException() {
        // Arrange
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        // Act
        EmployeeNotFoundException notFoundException = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(user.getUsername());
        });

        // Assert
        assertEquals(notFoundException.getMessage(),
                "Could not find Employee with user Username: " + user.getUsername());
        verify(users).getUserByUsername(user.getUsername());
    }

}

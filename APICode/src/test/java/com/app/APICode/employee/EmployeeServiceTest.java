package com.app.APICode.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.APICode.ctest.CtestRepository;
import com.app.APICode.notification.NotificationService;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.restaurant.RestaurantRepository;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.restaurant.RestaurantServiceImpl;
import com.app.APICode.user.User;
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
    public void addEmployee_NewUsername_ReturnSavedEmployee() {
        //Arrange
        employeeService.setRestaurants(restaurants);
        employeeService.setNotifications(notifications);

        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);

        long businessId = restaurant.getId();

        when(users.save(any(User.class))).thenReturn(user);
        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(restaurants.getRestaurantById(any(Long.class))).thenReturn(restaurant);
        when(notifications.addNewEmployeeApprovalNotification(any(String.class), any(Long.class), any(String.class))).thenReturn(null);
        //Act
        Employee savedEmployee = employeeService.addEmployeeToBusiness(user.getUsername(), "Manager", businessId);

        //Assert
        assertNotNull(savedEmployee);
        verify(users).save(user);
    }

    @Test
    public void addEmployee_InvalidRestaurantId_ReturnNull() {
        //Arrange
        employeeService.setRestaurants(restaurants);
        employeeService.setNotifications(notifications);

        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        long businessId = 10L;

        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(restaurants.getRestaurantById(any(Long.class))).thenReturn(null);
       
        //Act
        RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
            employeeService.addEmployeeToBusiness(user.getUsername(), "Manager", businessId);
        });

        //Assert
        assertEquals(notFoundException.getMessage(), "Could not find restaurant with ID: 10");
        verify(users).getUserByUsername(user.getUsername());
    }

    @Test
    public void approveEmployee_validUsername_ReturnEmployeeRole() {
        //Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Employee employee = new Employee(user,"HR Manager");
        employee.setRestaurant(restaurant);
        employee.setStatus("Pending");
        user.setEmployee(employee);

        when(users.getUserByUsername(any(String.class))).thenReturn(user);
        when(users.save(any(User.class))).thenReturn(user);

        //Act
        Employee approvedEmployee = employeeService.approveEmployee(user.getUsername());
        
        //Assert
        assertNotNull(approvedEmployee);
        assertEquals("Active",approvedEmployee.getStatus());


    }

    @Test
    public void deleteEmployee_validUsername_ReturnNull() {
        //Arrange
        User user = new User("pendingemployee@test.com", "user5", "User", "five", "", false, "ROLE_USER");
        Employee employee = new Employee(user,"HR Manager");
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");

        when(users.save(any(User.class))).thenReturn(user);
        when(users.getUserByUsername(any(String.class))).thenReturn(user);

        //Act
        employeeService.deleteEmployee(user.getUsername());
        Employee deletedEmployee = user.getEmployee();
        
        //Assert
        assertNull(deletedEmployee);
        verify(users).save(user);
    }

}

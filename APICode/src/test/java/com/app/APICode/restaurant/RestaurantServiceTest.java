package com.app.APICode.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;
import com.app.APICode.employee.EmployeeServiceImpl;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    
    @Mock
    private RestaurantRepository restaurants;

    @Mock 
    private EmployeeService employees;

    @Mock 
    private UserService users;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;
    @Test
    void getRestaurants_Success() {
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(restaurant);
        when(restaurants.findAll()).thenReturn(restaurantList);
        //Act
        List<RestaurantDTO> restaurantsList = restaurantService.listRestaurants();

        //Assert
        assertNotNull(restaurantsList);
        assertEquals(1,restaurantsList.size());
        verify(restaurants).findAll();
    }
    @Test
    void getRestaurant_ValidId_ReturnSavedRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        Long id = restaurant.getId();
       
        when(restaurants.findById(any(Long.class))).thenReturn(Optional.of(restaurant));
        
        //Act 
        Restaurant savedRestaurant = restaurantService.getRestaurantById(id);

        //Assert
        assertNotNull(savedRestaurant);
        assertEquals("Subway",savedRestaurant.getName());
        verify(restaurants).findById(id);
    }

    @Test
    void getRestaurant_InvalidId_ReturnException() {
         // Arrange
         Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
         Long id = 1000L;
 
         when(restaurants.findById(id)).thenReturn(Optional.empty());
         
           //Act
           RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
             restaurantService.getRestaurantById(id);
         });
 
         //Assert
         assertEquals(notFoundException.getMessage(), "Could not find restaurant with ID: " + id);
         verify(restaurants).findById(id);
    }

    @Test 
    void getRestaurant_ValidUsername_ReturnRestaurant() {
        //Arrange
        restaurantService.setEmployees(employees);
      
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        employee.setRestaurant(testRestaurant);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");
        users.save(user);

        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(user.getEmployee());

        //Act
        Restaurant savedRestaurant = restaurantService.getRestaurantByUsername(user.getUsername());

        //Assert
        assertNotNull(savedRestaurant);
        assertEquals("Subway",savedRestaurant.getName());
        verify(employees).getEmployeeByUsername(user.getUsername());
    }

    @Test
    void getRestaurant_NoRestaurant_ReturnException() {
        //Arrange
        restaurantService.setEmployees(employees);
      
        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        user.setEmployee(employee);
        user.setAuthorities("ROLE_EMPLOYEE");
        users.save(user);

        when(employees.getEmployeeByUsername(user.getUsername())).thenReturn(user.getEmployee());

        //Act
        RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.getRestaurantByUsername(user.getUsername());
        });

        //Assert
        assertEquals(notFoundException.getMessage(), "Could not find restaurant associated with username: " + user.getUsername());
        verify(employees).getEmployeeByUsername(user.getUsername());
    }

    @Test
    void getRestaurantOwner_ValidId_ReturnUser() {
        // Arrange
        restaurantService.setEmployees(employees);

        User user = new User("pendingemployee2@test.com", "user6", "User", "six", "testing23456", false, "ROLE_USER");
        Employee employee = new Employee(user, "HR Manager");
        Restaurant testRestaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        testRestaurant.setEmployees(employeeList);

        employee.setRestaurant(testRestaurant);
        user.setEmployee(employee);
        user.setAuthorities("ROLE_BUSINESS");

        when(restaurants.findById(any(Long.class))).thenReturn(Optional.of(testRestaurant));

        // Act
        User businessOwner = restaurantService.getRestaurantOwner(testRestaurant.getId());

        assertNotNull(businessOwner);
        verify(restaurants).findById(testRestaurant.getId());
    }

    @Test
    void addRestaurant_NewNameAndNewLocation_ReturnSavedRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);

        when(restaurants.findByNameAndLocation(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(restaurants.save(any(Restaurant.class))).thenReturn(restaurant);

        // Act
        Restaurant savedRestaurant = restaurantService.addRestaurant(restaurant);

        // Assert
        assertNotNull(savedRestaurant);
        verify(restaurants).findByNameAndLocation(restaurant.getName(), restaurant.getLocation());
        verify(restaurants).save(restaurant);
    }

    @Test
    void addRestaurant_SameID_ReturnNull() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        Restaurant restaurant2 = restaurant;

        when(restaurants.findByNameAndLocation(any(String.class), any(String.class))).thenReturn(Optional.of(restaurant));
        
        // Act
        RestaurantDuplicateException duplicateException = assertThrows(RestaurantDuplicateException.class, () -> {
            restaurantService.addRestaurant(restaurant2);
        });

        // Assert
        assertEquals(duplicateException.getMessage(), "This restaurant " + restaurant2.getName() + " exists at this location: " + restaurant2.getLocation());
        verify(restaurants).findByNameAndLocation(restaurant.getName(), restaurant.getLocation());
    }

    @Test
    void updateRestaurant_ValidId_ReturnUpdatedRestaurant() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
     
        Restaurant restaurant2 = restaurant;
        restaurant2.setLocation("Plaza Singapura");
        Long id = restaurant2.getId();

        when(restaurants.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurants.save(restaurant)).thenReturn(restaurant);
        
        //Act
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, restaurant2);
        
        //Assert
        assertNotNull(updatedRestaurant);
        assertEquals("Plaza Singapura", restaurant.getLocation());
        verify(restaurants).findById(id);
        verify(restaurants).save(restaurant);
    }

    @Test
    void updateRestaurant_InvalidId_ReturnException() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        Restaurant restaurant2 = restaurant;
        restaurant2.setLocation("Plaza Singapura");
        Long id = 1000L;

        when(restaurants.findById(id)).thenReturn(Optional.empty());
        
        //Act
          RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.updateRestaurant(id, restaurant2);
        });

        //Assert
        assertEquals(notFoundException.getMessage(), "Could not find restaurant with ID: " + restaurant2.getId());
        verify(restaurants).findById(id);
    }

    @Test
    void deleteRestaurant_ValidId_ReturnNull() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        Long id = restaurant.getId();
        when(restaurants.existsById(id)).thenReturn(true);
       //Act
       restaurantService.removeById(id);

       //Assert
       verify(restaurants).deleteById(id);
    }

    @Test
    void deleteRestaurant_InvalidId_ReturnException() {
        //Arrange
        
        Long id = 1000L;

        //Act
        RestaurantNotFoundException notFoundException = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.removeById(id);
        });

        //Assert
        assertEquals(notFoundException.getMessage(), "Could not find restaurant with ID: " + id);
    }
}

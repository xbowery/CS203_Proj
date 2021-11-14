package com.app.APICode.crowdlevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.app.APICode.employee.Employee;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserForbiddenException;
import com.app.APICode.user.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CrowdLevelServiceTest {
    
    @Mock
    private CrowdLevelRepository crowdLevels;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private CrowdLevelServiceImpl crowdLevelService;

    @Test
    void getCrowdLevel_ValidBusinessOwner_ReturnList() {
        // Arrange
        Restaurant restaurant = new Restaurant("Subway", "SMU SCIS", "Western", "Fast food chain", 50);
        List<Employee> employeeList = new ArrayList<>();

        User businessOwner = new User("businesstest@test.com", "businesstest", "business", "test", "password", false, "ROLE_BUSINESS");
        Employee owner = new Employee(businessOwner, "Owner");
        owner.setRestaurant(restaurant);
        businessOwner.setEmployee(owner);

        employeeList.add(owner);
        restaurant.setEmployees(employeeList);

        CrowdLevel crowdLevel = new CrowdLevel(new Date(System.currentTimeMillis()), "Low", 20, restaurant);
        List<CrowdLevel> crowdLevelListMock = new ArrayList<>();
        crowdLevelListMock.add(crowdLevel);

        when(userService.getUserByUsername(anyString())).thenReturn(businessOwner);
        when(crowdLevels.findByRestaurant(any(Restaurant.class))).thenReturn(crowdLevelListMock);

        // Act
        List<CrowdLevel> crowdLevelList = crowdLevelService.listCrowdLevelByEmployee(businessOwner.getUsername());

        // Assert
        assertEquals(crowdLevelListMock, crowdLevelList);
        verify(crowdLevels).findByRestaurant(restaurant);
    }

    @Test
    public void getCrowdLevel_NormalUser_Forbidden() {
        // Arrange
        User normalUser = new User("normalguy@test.com", "normaluser", "normal", "user", "password", false, "ROLE_USER");

        when(userService.getUserByUsername(anyString())).thenReturn(normalUser);

        // Act
        UserForbiddenException forbiddenException = assertThrows(UserForbiddenException.class, () -> {
            crowdLevelService.listCrowdLevelByEmployee(normalUser.getUsername());
        });

        // Assert
        assertEquals(forbiddenException.getMessage(), "You are forbidden from processing this request.");
    }
}

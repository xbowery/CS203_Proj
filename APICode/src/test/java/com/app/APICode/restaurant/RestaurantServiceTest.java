package com.app.APICode.restaurant;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    
    @Mock
    private RestaurantRepository restaurants;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

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

    // TODO: Add more tests
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
}

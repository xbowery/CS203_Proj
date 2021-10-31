package com.app.APICode.restaurant;

import java.util.List;
import java.util.stream.Collectors;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurants;
    private EmployeeService employees;

    // for testing purposes
    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
        restaurants.save(new Restaurant("Astons", "Cathay", "Western", "Western steakhouse", 50));
        restaurants.save(new Restaurant("Dian Xiao Er", "Hillion", "Chinese",
                "Chinese dining experience in an ancient Inn", 50));
    }

    // To break circular dependency
    @Autowired
    public void setEmployees(EmployeeService employees) {
        this.employees = employees;
    }

    public EmployeeService getEmployees() {
        return this.employees;
    }

    @Override
    // public List<Restaurant> listRestaurants() {
    //     return restaurants.findAll();
    // }
    public List<RestaurantDTO> listRestaurants() {
        List<Restaurant> restaurantsList = restaurants.findAll();
        return restaurantsList.stream().map(this::convertToRestaurantDTO).collect(Collectors.toList());
    }

    private RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return RestaurantDTO.convertToRestaurantDTO(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);  
        if (restaurant == null)
            throw new RestaurantNotFoundException(id);
        return restaurant;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        Restaurant duplicate = restaurants.findByNameAndLocation(restaurant.getName(), restaurant.getLocation())
                .orElse(null);
        if (duplicate != null)
            throw new RestaurantDuplicateException(duplicate.getName(), duplicate.getLocation());
        return restaurants.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(long id, Restaurant updatedRestaurant) {
        Restaurant restaurant = restaurants.findById(id).orElse(null);
        if (restaurant == null)
            throw new RestaurantNotFoundException(updatedRestaurant.getId());

        restaurant.setName(updatedRestaurant.getName());
        restaurant.setLocation(updatedRestaurant.getLocation());
        restaurant.setCuisine(updatedRestaurant.getCuisine());
        restaurant.setDescription(updatedRestaurant.getDescription());
        restaurant.setMaxCapacity(updatedRestaurant.getMaxCapacity());
        return restaurants.save(restaurant);
    }

    @Override
    @Transactional
    public void removeById(long id) {
        try {
            restaurants.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantNotFoundException(id);
        }
    }

    public Restaurant getRestaurantByUsername(String username) {
        Employee employee = employees.getEmployeeByUsername(username);

        Restaurant restaurant = employee.getRestaurant();
        if (restaurant == null) {
            throw new RestaurantNotFoundException(username);
        }

        return restaurant;
    }
}

package com.app.APICode.restaurant;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.dao.EmptyResultDataAccessException;

@RestController
public class RestaurantController {
    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    /**
     * List all restaurants in the system
     * @return list of all restaurants
     */
    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantService.listRestaurants();
    }

    /**
     * Search for a restaurant with the given name and location (identifier defined for a restaurant)
     * If there is no restaurant with the given "name" and "location", throw a RestaurantNotFoundException
     * @param name name of restaurant
     * @param location location of restaurant
     * @return restaurant with the given name and location
     */
    @GetMapping("/restaurants/{name}/{location}")
    public Restaurant getRestaurant(@PathVariable String name, @PathVariable String location) {
        Restaurant restaurant = restaurantService.getRestaurant(name,location);

        if (restaurant== null) throw new RestaurantNotFoundException(name,location);
        return restaurantService.getRestaurant(name, location);
    }

    /**
     * Add new restaurant with POST request to "/restaurants"
     * @param restaurant a Restaurant object containing the info to be added
     * @return the newly added restaurant
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@Valid @RequestBody Restaurant restaurant){
        Restaurant savedRestaurant = restaurantService.addRestaurant(restaurant);
        if (savedRestaurant == null) throw new RestaurantDuplicateException(restaurant.getName(),restaurant.getLocation());
        return savedRestaurant;
    }

    /**
     * Search for user given the name and location and updates the relevant restaurant details
     * If there is no restaurant with the given name and location, throw a RestaurantNotFoundException
     * @param name name of restaurant
     * @param location location of restaurant
     * @param newRestaurantInfo a Restaurant object containing the new restaurant info to be updated
     * @return the updated restaurant
     */
    @PutMapping("/restaurants/{name}/{location}")
    public Restaurant updateRestaurant(@PathVariable String name, @PathVariable String location, @Valid @RequestBody Restaurant newRestaurantInfo) {
        Restaurant restaurant = restaurantService.updateRestaurant(name,location, newRestaurantInfo);
        if (restaurant == null) throw new RestaurantNotFoundException(name,location);

        return restaurant;
    }
    /**
     * Remove a restaurant with the DELETE request to "/restaurants/{name}/{location}"
     * If there is no restaurant with the given "name" and "location", throw a RestaurantNotFoundException
     * @param name name of restaurant
     * @param location location of restaurant
     */
    @Transactional
    @DeleteMapping("/restaurants/{name}/{location}")
    public void deleteRestaurant(@PathVariable String name, @PathVariable String location) {
        try {
            restaurantService.deleteRestaurant(name,location);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantNotFoundException(name,location);
        }
    }
   
}



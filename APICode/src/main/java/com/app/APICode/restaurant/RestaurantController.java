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

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantService.listRestaurants();
    }

    @GetMapping("/restaurants/{name}/{location}")
    public Restaurant getRestaurant(@PathVariable String name, @PathVariable String location) {
        Restaurant restaurant = restaurantService.getRestaurant(name,location);

        if (restaurant== null) throw new RestaurantNotFoundException(name,location);
        return restaurantService.getRestaurant(name, location);
    }

    /**
    * @param restaurant
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@Valid @RequestBody Restaurant restaurant){
        Restaurant savedRestaurant = restaurantService.addRestaurant(restaurant);
        if (savedRestaurant == null) throw new RestaurantDuplicateException(restaurant.getName(),restaurant.getLocation());
        return savedRestaurant;
    }

    /**
     * If there is no restaurant with the given name and location, throw a RestaurantNotFoundException
     * @param name
     * @param location
     * @param newRestaurantInfo
     * @return the updated restaurant
     */
    @PutMapping("/restaurants/{name}/{location}")
    public Restaurant updateRestaurant(@PathVariable String name, @PathVariable String location, @Valid @RequestBody Restaurant newRestaurantInfo) {
        Restaurant restaurant = restaurantService.updateRestaurant(name,location, newRestaurantInfo);
        if (restaurant == null) throw new RestaurantNotFoundException(name,location);

        return restaurant;
    }

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



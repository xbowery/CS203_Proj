package com.app.APICode.restaurant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {
    private RestaurantService restaurantService;
    private RestaurantRepository restaurants;
    private UserRepository users;

    public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurants, UserRepository users) {
        this.restaurantService = restaurantService;
        this.restaurants = restaurants;
        this.users = users;
    }

    /**
     * GET a list of all restaurants
     * 
     * @return list of all {@link Restaurant}
     */
    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.listRestaurants();
    }

    /**
     * GET a restaurant with the given id
     * 
     * @param id of restaurant
     * @return {@link Restaurant} with the given id
     * @throws RestaurantNotFoundException in case a restaurant with the provided
     *                                     {@literal id} does not exist
     */
    @GetMapping("/restaurants/{id}")
    public Restaurant getRestaurant(@PathVariable long id) {
        return restaurantService.getRestaurant(id);
    }

    @GetMapping("/restaurants/user/{username}")
    public Restaurant getRestaurant(@PathVariable (value = "username") String username) {
        Optional<User> user = users.findByUsername(username);
        if(!user.isPresent()){
            throw new UserNotFoundException(username);
        }
        
        Employee employee = user.get().getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }

        Restaurant restaurant = employee.getRestaurant();
        if(restaurant == null){
            throw new RestaurantNotFoundException(username); 
        }

        return restaurant;
    }

    /**
     * Add new restaurant with POST request
     * 
     * @param restaurant {@link Restaurant} containing the info to be added
     * @return the newly added restaurant
     * @throws RestaurantDuplicateException in case a restaurant with the same
     *                                      {@literal id} exist
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@Valid @RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    /**
     * Updates restaurant with PUT request to "/restaurants/{id}"
     * 
     * @param id                of the {@link Restaurant}
     * @param updatedRestaurant {@link Restaurant} containing the updated
     *                          information
     * @return the updated {@link Restaurant}
     * @throws RestaurantNotFoundException in case a restaurant with the provided
     *                                     {@literal id} does not exist
     */
    @PutMapping("/restaurants/{id}")
    public Restaurant updateRestaurant(@PathVariable long id, @Valid @RequestBody Restaurant updatedRestaurant) {
        return restaurantService.updateRestaurant(id, updatedRestaurant);
    }

    /**
     * Removes a {@link Restaurant} with DELETE request to "/restaurants/{id}"
     * 
     * @param id of restaurant
     * @throws RestaurantNotFoundException in case a restaurant with the provided
     *                                     {@literal id} does not exist
     */
    @DeleteMapping("/restaurants/{id}")
    public void deleteRestaurant(@PathVariable long id) {
        restaurantService.removeById(id);
    }
}

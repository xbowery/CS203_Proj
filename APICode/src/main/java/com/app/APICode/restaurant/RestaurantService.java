package com.app.APICode.restaurant;

import java.util.List;

import com.app.APICode.user.User;

/**
 * Service interface layer that performs CRUD operations for {@link Restaurant}
 */
public interface RestaurantService {

    /**
     * Retrieves all restaurants registered.
     * 
     * @return a list of {@link RestaurantDTO} 
     */
    List<RestaurantDTO> listRestaurants();

    /**
     * Gets the Restaurant associated with the given id.
     * <p>If no Restaurant found, throw a {@link RestaurantNotFoundException}.
     * 
     * @param id restaurant id
     * @return a {@link Restaurant} object
     */
    Restaurant getRestaurantById(long id);

    /**
     * Gets the Restaurant associated with the given "username", to find the Restaurant to which Employee belongs to.
     * <p>If no Restaurant found, throw a
     * {@link RestaurantNotFoundException}.
     * 
     * @param username a String containing the username
     * @return a {@link Restaurant} object
     */
    Restaurant getRestaurantByUsername(String username);

    /**
     * Creates a new restaurant with the given name and location. 
     * <p>If Restaurant with given name and location already exists, throw a
     * {@link RestaurantDuplicateException}.
     * 
     * @param restaurant a {@link Restaurant} object
     * @return the newly added {@link Restaurant} object
     */
    Restaurant addRestaurant(Restaurant restaurant);

    /**
     * Updates the Restaurant info with the given Restaurant object by id. 
     * <p>If no Restaurant exists with the given id,
     * throw a {@link RestaurantNotFoundException}.
     * 
     * @param id restaurant id
     * @param restaurant    a {@link Restaurant} object containning the new info to be updated
     * @return an updated {@link Restaurant} object
     */
    Restaurant updateRestaurant(long id, Restaurant restaurant);

    /**
     * Deletes the Restaurant with the given id. 
     * <p>If Restaurant does not exist, throw a
     * {@link RestaurantNotFoundException}.
     * 
     * @param id restaurant id
     */
    void removeById(long id);

    /**
     * Gets the User who is the Restaurant Owner associated with the given id. 
     * <p>If no User found, throw a
     * {@link RestaurantOwnerNotFoundException}.
     * 
     * @param id restaurant id
     * @return a {@link User} object
     */
    User getRestaurantOwner(long id);
}

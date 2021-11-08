package com.app.APICode.restaurant;

import java.util.List;

import com.app.APICode.user.User;

public interface RestaurantService {

    List<RestaurantDTO> listRestaurants();
    Restaurant getRestaurantById(long id);
    Restaurant getRestaurantByUsername(String username);
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(long id, Restaurant restaurant);
    void removeById(long id);
    User getRestaurantOwner(long id);
}

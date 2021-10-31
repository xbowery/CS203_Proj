package com.app.APICode.restaurant;

import java.util.List;

public interface RestaurantService {

    List<RestaurantDTO> listRestaurants();
    Restaurant getRestaurantById(long id);
    Restaurant getRestaurantByUsername(String username);
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(long id, Restaurant restaurant);
    void removeById(long id);
}

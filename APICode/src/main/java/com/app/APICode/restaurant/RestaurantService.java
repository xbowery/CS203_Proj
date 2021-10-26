package com.app.APICode.restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> listRestaurants();
    Restaurant getRestaurant(long id);
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(long id, Restaurant restaurant);
    void removeById(long id);
}

package com.app.APICode.restaurant;

import java.util.*;

public interface RestaurantService {
    List<Restaurant> listRestaurants();
    Restaurant getRestaurant(String name, String location);
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(String name, String location, Restaurant restaurant);
    void deleteRestaurant(String name, String location);
}

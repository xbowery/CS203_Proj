package com.app.APICode.restaurant;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    private RestaurantRepository restaurants;

    //for testing purposes 
    public RestaurantServiceImpl(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
        restaurants.save(new Restaurant("Astons", "Cathay", "Western", "Western steakhouse", 50));
        restaurants.save(new Restaurant("Dian Xiao Er", "Hillion", "Chinese","Chinese dining experience in an ancient Inn",50));
    }

    @Override
    public List<Restaurant> listRestaurants() {
        return restaurants.findAll();
    }

    @Override 
    public Restaurant getRestaurant(String name, String location){
        return restaurants.findByNameAndLocation(name, location).orElse(null);
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        List<Restaurant> sameRestaurantNameAndLocations = restaurants.findByNameAndLocation(restaurant.getName(), restaurant.getLocation())
        .map(Collections::singletonList)
        .orElseGet(Collections::emptyList);

        if (sameRestaurantNameAndLocations.size() == 0) {
            return restaurants.save(restaurant);
        } else {
            return null;
        }
    }

    @Override
    public Restaurant updateRestaurant(String name, String location, Restaurant newRestaurantInfo) {
        return restaurants.findByNameAndLocation(name, location).map(restaurant -> {
            restaurant.setName(newRestaurantInfo.getName());
                restaurant.setLocation(newRestaurantInfo.getLocation());
                restaurant.setCuisine(newRestaurantInfo.getCuisine());
                restaurant.setDescription(newRestaurantInfo.getDescription());
                restaurant.setMaxCapacity(newRestaurantInfo.getMaxCapacity());
                return restaurants.save(restaurant);
        }).orElse(null);
    }

    @Override 
    public void deleteRestaurant(String name, String location) {
        //using iterator to iterate through the restaurants
        restaurants.deleteByNameAndLocation(name, location);
    }
}

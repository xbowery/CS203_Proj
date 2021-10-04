package com.app.APICode.restaurant;

public class RestaurantNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(String name, String location) {
        super("Could not find restaurant name: " + name + " at this location: " + location);
    }
}

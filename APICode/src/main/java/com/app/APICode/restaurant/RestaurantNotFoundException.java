package com.app.APICode.restaurant;

public class RestaurantNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(long id) {
        super("Could not find restaurant with ID: " + id);
    }
    public RestaurantNotFoundException(String username) {
        super("Could not find restaurant associated with username: " + username);
    }
}

package com.app.APICode.restaurant;

public class RestaurantOwnerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public RestaurantOwnerNotFoundException(long id) {
        super("Could not find Owner with Restaurant ID: " + id);
    }
}

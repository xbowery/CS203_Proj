package com.app.APICode.restaurant;

public class RestaurantDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestaurantDuplicateException(String name, String location) {
        super("This restaurant " + name + " exists at this location: " + location);
    }
}
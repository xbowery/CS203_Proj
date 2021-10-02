package com.app.APICode.restaurant;

public class RestaurantDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestaurantDuplicateException(String name) {
        super("This restaurant exists: " + name);
    }
}
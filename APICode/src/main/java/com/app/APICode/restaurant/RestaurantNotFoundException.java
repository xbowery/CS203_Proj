package com.app.APICode.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a {@link Restaurant} is not found in the repository
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RestaurantNotFoundException(long id) {
        super("Could not find restaurant with ID: " + id);
    }
    public RestaurantNotFoundException(String username) {
        super("Could not find restaurant associated with username: " + username);
    }
}

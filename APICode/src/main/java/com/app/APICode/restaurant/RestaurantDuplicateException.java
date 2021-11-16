package com.app.APICode.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a same {@link Restaurant} is found in the repository
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RestaurantDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestaurantDuplicateException(String name, String location) {
        super("This restaurant " + name + " exists at this location: " + location);
    }
}
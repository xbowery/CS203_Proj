package com.app.APICode.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantOwnerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public RestaurantOwnerNotFoundException(long id) {
        super("Could not find Owner with Restraunt ID: " + id);
    }
}

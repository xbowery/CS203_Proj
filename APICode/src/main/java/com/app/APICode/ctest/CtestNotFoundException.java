package com.app.APICode.ctest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a {@link Ctest} is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class CtestNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CtestNotFoundException(Long id) {
        super("Could not find Covid Test " + id);
    }
}


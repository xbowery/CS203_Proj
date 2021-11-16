package com.app.APICode.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an invalid JWT is sent from a browser to 
 * the server via HTTP request header 
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException() {
        super("Invalid JWT!");
    }
}

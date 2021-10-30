package com.app.APICode.security.exception;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException() {
        super("Invalid JWT!");
    }
}

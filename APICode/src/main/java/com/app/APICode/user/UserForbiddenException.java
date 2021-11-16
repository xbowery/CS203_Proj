package com.app.APICode.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a {@link User} is forbidden from executing an action
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserForbiddenException(String errorMessage) {
        super(errorMessage);
    }
}

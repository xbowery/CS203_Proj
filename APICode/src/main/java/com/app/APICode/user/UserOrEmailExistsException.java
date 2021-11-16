package com.app.APICode.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the username or email already belongs to a {@link User} in the repository.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserOrEmailExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserOrEmailExistsException(String errorMessage) {
        super(errorMessage);
    }
}

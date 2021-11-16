package com.app.APICode.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is forbidden from processing a request that requires 
 * elevated authorisation for {@link Employee} class
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmployeeForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeForbiddenException(String errorMessage) {
        super(errorMessage);
    }
}

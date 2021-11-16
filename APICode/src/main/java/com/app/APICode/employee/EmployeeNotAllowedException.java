package com.app.APICode.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an {@link Employee} is forbidden from processing a request
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmployeeNotAllowedException extends RuntimeException {
    public EmployeeNotAllowedException(String message) {
        super(message);
    }
}

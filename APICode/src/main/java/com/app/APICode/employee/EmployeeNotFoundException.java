package com.app.APICode.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an {@link Employee} is not found in the repository
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(Long id) {
        super("Could not find Employee with id: " + id);
    }

    public EmployeeNotFoundException(String username) {
        super("Could not find Employee with user Username: " + username);
    }
}
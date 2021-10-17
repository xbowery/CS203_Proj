package com.app.APICode.employee;

public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(Long id) {
        super("Could not find Employee with id: " + id);
    }

    public EmployeeNotFoundException(String username) {
        super("Could not find Employee with user Username: " + username);
    }
}
package com.app.APICode.employee;

public class EmployeeNotAllowedException extends RuntimeException {
    public EmployeeNotAllowedException(String message) {
        super(message);
    }
}

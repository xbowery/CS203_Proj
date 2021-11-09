package com.app.APICode.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmployeeNotAllowedException extends RuntimeException {
    public EmployeeNotAllowedException(String message) {
        super(message);
    }
}

package com.app.APICode.employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> listEmployees(Long user_id);
}

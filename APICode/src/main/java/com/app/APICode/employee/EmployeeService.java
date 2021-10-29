package com.app.APICode.employee;

import java.util.List;
import java.util.Optional;

import com.app.APICode.user.User;

public interface EmployeeService {
    List<User> getAllEmployeesByBusinessOwner(String username);
    Employee getEmployeeByUsername(String username);
    Employee addEmployeeToBusiness(String username, String designation, long businessId);
    Employee approveEmployee(String username);
    Employee deleteEmployee(String username);
}

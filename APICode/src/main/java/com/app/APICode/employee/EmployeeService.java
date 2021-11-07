package com.app.APICode.employee;

import java.util.List;

import com.app.APICode.ctest.Ctest;
import com.app.APICode.user.UserDTO;

public interface EmployeeService {
    List<UserDTO> getAllEmployeesByBusinessOwner(String username);
    Employee getEmployeeByUsername(String username);
    Employee addEmployeeToBusiness(String username, String designation, long businessId);
    Employee approveEmployee(String username);
    Employee deleteEmployee(String username);
    List<Ctest> getAllEmployeesCtest(String username);
}

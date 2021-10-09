package com.app.APICode.employee;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employees;

    @Override
    public List<Employee> listEmployees(Long user_id) {
        Long restaurant_id = employees.findRestaurantIdByUserId(user_id).orElse(null);
        
        if (restaurant_id == null) {
            return null;
        }

        List<Employee> employeeList = employees.findByRestaurant(restaurant_id).orElse(null);

        return employeeList;
    }
}

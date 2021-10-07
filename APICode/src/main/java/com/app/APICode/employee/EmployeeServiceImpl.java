package com.app.APICode.employee;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employees;

    @Override
    public List<Employee> listEmployees(Long user_id) {
        Long restaurant_id = employees.findRestaurantByUser(user_id).orElse(null);
        
        if (restaurant_id == null) {
            return null;
        }

        List<Employee> employeeList = employees.findByRestaurant(restaurant_id).orElse(null);

        return employeeList;
    }
}

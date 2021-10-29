package com.app.APICode.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employees;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employees){
        this.employees = employees;
    }

    @Override
    public List<Employee> listEmployees(Long user_id) {
        Long restaurant_id = employees.findRestaurantIdByUserId(user_id).orElse(null);
        
        if (restaurant_id == null) {
            return null;
        }

        List<Employee> employeeList = employees.findByRestaurant(restaurant_id).orElse(null);

        return employeeList;
    }

    @Override
    public void save(Employee employee){
        employees.save(employee);
        System.out.println("Employee Created");
    }
}

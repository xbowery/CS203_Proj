package com.app.APICode.employee;

import java.util.List;
import java.util.stream.Collectors;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private UserService users;
    private RestaurantService restaurants;

    @Autowired
    public EmployeeServiceImpl(UserService users, RestaurantService restaurants){
        this.users = users;
        this.restaurants = restaurants;
    }

    @Override
    public List<User> getAllEmployeesByBusinessOwner(String username) {
        Employee owner = getEmployeeByUsername(username);
        return owner.getRestaurant().getEmployees().stream().map(employee -> employee.getUser()).collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        User user = users.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        Employee employee = user.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException(username);
        }
        return employee;
    }

    @Override
    public Employee addEmployeeToBusiness(String username, String designation, long businessId) {
        User user = users.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }

        Restaurant restaurant = restaurants.getRestaurantById(businessId);

        Employee employee = new Employee(user, designation);
        employee.setRestaurant(restaurant);
        employee.setStatus("Pending");
        user.setEmployee(employee);

        return users.save(user).getEmployee();
    }

    // @Override
    // public Optional<Employee> findById(long employeeId){
    //     return employees.findById(employeeId);
    // }
}

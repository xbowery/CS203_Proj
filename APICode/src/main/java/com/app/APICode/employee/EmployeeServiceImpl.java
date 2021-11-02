package com.app.APICode.employee;

import java.util.List;
import java.util.stream.Collectors;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private UserService users;
    private RestaurantService restaurants;

    @Autowired
    public EmployeeServiceImpl(UserService users) {
        this.users = users;
    }

    // To break circular dependency
    @Autowired
    public void setRestaurants(RestaurantService restaurants) {
        this.restaurants = restaurants;
    }

    public RestaurantService getRestaurants() {
        return restaurants;
    }

    @Override
    public List<User> getAllEmployeesByBusinessOwner(String username) {
        Employee owner = getEmployeeByUsername(username);
        return owner.getRestaurant().getEmployees().stream().map(employee -> employee.getUser())
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        User user = users.getUserByUsername(username);
        Employee employee = user.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException(username);
        }
        return employee;
    }

    @Override
    public Employee addEmployeeToBusiness(String username, String designation, long businessId) {
        User user = users.getUserByUsername(username);
        Restaurant restaurant = restaurants.getRestaurantById(businessId);

        Employee employee = new Employee(user, designation);
        employee.setRestaurant(restaurant);
        employee.setStatus("Pending");
        user.setEmployee(employee);

        return users.save(user).getEmployee();
    }

    @Override
    public Employee approveEmployee(String username) {
        User user = users.getUserByUsername(username);
        user.setAuthorities("ROLE_EMPLOYEE");

        Employee employee = user.getEmployee();
        employee.setStatus("Active");

        users.save(user);
        return employee;
    }

    @Override
    public Employee deleteEmployee(String username) {
        User user = users.getUserByUsername(username);

        user.setAuthorities("ROLE_USER");
        Employee employee = user.getEmployee();
        employee.setRestaurant(null);
        user.setEmployee(null);
        users.save(user);
        return employee;
    }

}

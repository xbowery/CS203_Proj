package com.app.APICode.employee;

import java.util.List;
import java.util.stream.Collectors;

import com.app.APICode.notification.NotificationService;
import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;
import com.app.APICode.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private UserService users;
    private RestaurantService restaurants;
    private NotificationService notifications;

    public EmployeeServiceImpl() {
    }

    // To break circular dependency
    @Autowired
    public void setRestaurants(RestaurantService restaurants) {
        this.restaurants = restaurants;
    }

    @Autowired
    public void setNotifications(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Autowired
    public void setUsers(UserService users) {
        this.users = users;
    }

    public RestaurantService getRestaurants() {
        return restaurants;
    }

    @Override
    public List<UserDTO> getAllEmployeesByBusinessOwner(String username) {
        Employee owner = getEmployeeByUsername(username);
        return owner.getRestaurant().getEmployees().stream().map(employee -> UserDTO.convertToUserDTO(employee.getUser()))
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
    public Employee getEmployeeDetailsByUsername(String requesterUsername, String employeeUsername) {
        Employee businessOwner = getEmployeeByUsername(requesterUsername);
        Employee employee = getEmployeeByUsername(employeeUsername);

        if (!(businessOwner.getRestaurant().equals(employee.getRestaurant()))) {
            throw new EmployeeNotAllowedException("You are unauthorised to perform this action.");
        }

        return employee;
    }

    @Override
    public Employee addEmployeeToBusiness(String username, String designation, long businessId) {
        User user = users.getUserByUsername(username);
        Restaurant restaurant = restaurants.getRestaurantById(businessId);

        if (restaurant == null) {
            throw new RestaurantNotFoundException(businessId);
        }

        Employee employee = new Employee(user, designation);
        employee.setRestaurant(restaurant);
        employee.setStatus("Pending");
        user.setEmployee(employee);
        notifications.addNewEmployeeApprovalNotification(username, businessId);

        return users.save(user).getEmployee();
    }

    @Override
    public Employee approveEmployee(String username) {
        User user = users.getUserByUsername(username);
        Employee employee = user.getEmployee();

        user.setAuthorities("ROLE_EMPLOYEE");

        Employee employee = user.getEmployee();
        employee.setStatus("Approved");

        users.save(user);
        return employee;
    }

    @Override
    public void deleteEmployee(String username) {
        User user = users.getUserByUsername(username);
        //check if user is an employee
        if (user.getEmployee() == null) {
            throw new EmployeeNotFoundException(username);
        }
        user.setAuthorities("ROLE_USER");
        Employee employee = user.getEmployee();
        employee.setRestaurant(null);
        user.setEmployee(null);
        users.save(user);
    }

    @Override
    public List<Ctest> getAllEmployeesCtest(String username) {
        List<Ctest> allCtests = new ArrayList<Ctest>();

        Employee owner = getEmployeeByUsername(username);
        List<Employee> employees = owner.getRestaurant().getEmployees();
        for (Employee e : employees) {
            if(e.getCtests().size() > 0){
                allCtests.add(e.getCtests().get(e.getCtests().size() - 1));
            }
        }
        return allCtests;
    }

    
}

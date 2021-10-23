package com.app.APICode.employee;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private UserService userService;
    private EmployeeService employeeService;

    public EmployeeController(UserService userService, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    /**
     * List all employees of a particular business in the database
     * 
     * @param principal name of the user logged in currently
     * @return list of employees in a particular business
     */
    @GetMapping("/employees") 
    public List<User> getEmployees(Principal principal) {
        Long principal_id = userService.getUserIdByUsername(principal.getName());
    
        List<Employee> employeeList = employeeService.listEmployees(principal_id);
        List<User> userList = new ArrayList<>();
        for (Employee employee : employeeList) {
            Long user_id = employee.getUser().getId();
            User user = userService.getUserById(user_id);
            userList.add(user);
        }
        return userList;
    }

    @GetMapping("/employees/{username}")
    public List<User> getEmployees2(@PathVariable String username){
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);

        Employee owner = user.getEmployee();
        if (owner == null)
            throw new EmployeeNotFoundException(username);
        
        Restaurant restaurant = owner.getRestaurant();
        List<Employee> employeeList = restaurant.getEmployees();

        List<User> userList = new ArrayList<>();
        for (Employee employee : employeeList) {
            Long user_id = employee.getUser().getId();
            User userTemp = userService.getUserById(user_id);
            userList.add(userTemp);
        }
        return userList;
        
    }

    /**
     * Search for employee with the given username
     * If there is no user with the given username, throw a UserNotFoundException
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return employee with the given username
     */
    @GetMapping("/users/{username}/employee")
    public Employee getEmployee(@PathVariable String username){
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);

        Employee employee = user.getEmployee();
        if (employee == null)
            throw new EmployeeNotFoundException(username);

        return user.getEmployee();
    }

    /**
     * Add new employee with POST request to "/users/{username}/employee"
     * If there is no user with the given username, throw a UserNotFoundException
     * 
     * @param username username of employee
     * @return the newly added employee
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{username}/employee")
    public Employee addEmployee(@PathVariable (value = "username") String username){
        User user = userService.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }
        Employee employee = new Employee(user);
        user.setEmployee(employee);
        userService.save(user);
        return employee;
    }
}

package com.app.APICode.employee;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.restaurant.RestaurantService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
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
        return employeeService.getAllEmployeesByBusinessOwner(principal.getName());
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
    public Employee getEmployee(Principal principal){
        return employeeService.getEmployeeByUsername(principal.getName());
    }

    /**
     * Add new employee with POST request to "/users/{username}/employee/{restaurantId}"
     * If there is no user with the given username, throw a UserNotFoundException
     * 
     * 
     * @param username username of employee
     * @param restrauntId id of the restraunt the employee wants to apply to
     * @param designation Designation of the employee
     * @return the newly added employee
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{username}/employee/{restaurantId}")
    public Employee postEmployee(Principal principal, @PathVariable (value = "restaurantId") long restaurantId, @RequestBody String designation){
        return employeeService.addEmployeeToBusiness(principal.getName(), designation, restaurantId);
    }

    /**
     * Search for employee with the given username
     * If there is no user with the given username, throw a UserNotFoundException
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return employee with the given username
     */
    // @PutMapping("/users/employee/{employeeId}")
    // public Employee editEmployeeStatus(@PathVariable (value = "employeeId") long employeeId){
    //     Optional<Employee> employeeOp = employeeService.findById(employeeId);
    //     if(!employeeOp.isPresent()){
    //         throw new EmployeeNotFoundException(employeeId);
    //     }
    //     Employee employee = employeeOp.get();
    //     employee.setStatus("Active");
    //     User user = employee.getUser();
    //     user.setAuthorities("ROLE_EMPLOYEE");
    //     return employee;
    // }

    /**
     * Search for employee with the given username
     * If there is no user with the given username, throw a UserNotFoundException
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return employee with the given username
     */
    // @DeleteMapping("/users/employee/{employeeId}")
    // public Employee deleteEmployee(@PathVariable (value = "employeeId") long employeeId){
    //     Optional<Employee> employeeOp = employeeService.findById(employeeId);
    //     if(!employeeOp.isPresent()){
    //         throw new EmployeeNotFoundException(employeeId);
    //     }
    //     Employee employee = employeeOp.get();
    //     return employee;
    // }
}

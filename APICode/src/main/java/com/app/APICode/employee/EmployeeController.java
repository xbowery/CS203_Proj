package com.app.APICode.employee;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Employee", description = "Employee API")
public class EmployeeController {
    private UserService userService;
    private EmployeeService employeeService;

    public EmployeeController(UserService userService, EmployeeService employeeService,
            EmployeeRepository employeeRepository) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    /**
     * List all employees of a particular business in the database
     * 
     * @param principal name of the user logged in currently
     * @return list of employees in a particular business
     */
    @Operation(summary = "List all Employees", description = "List all employees by the Restuarant that is owned by the User", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))), })
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

    /**
     * Search for employee with the given username If there is no user with the
     * given username, throw a UserNotFoundException If there is no employee with
     * the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return employee with the given username
     */
    @Operation(summary = "Get Employee details", description = "Get employee details by username", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))), })
    @GetMapping("/users/{username}/employee")
    public Employee getEmployee(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);

        Employee employee = user.getEmployee();
        if (employee == null)
            throw new EmployeeNotFoundException(username);

        return user.getEmployee();
    }

    /**
     * Add new employee with POST request to "/users/{username}/employee" If there
     * is no user with the given username, throw a UserNotFoundException
     * 
     * @param username username of employee
     * @return the newly added employee
     */
    @Operation(summary = "Upgrade user to Employee", description = "Find user by username and upgrade them to Employee", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful add new Employee", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))), })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{username}/employee")
    public Employee addEmployee(@PathVariable(value = "username") String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        Employee employee = new Employee(user);
        user.setEmployee(employee);
        userService.save(user);
        return employee;
    }
}

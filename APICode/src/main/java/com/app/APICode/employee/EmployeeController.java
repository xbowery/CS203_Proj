package com.app.APICode.employee;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.app.APICode.employee.message.RequestMessage;
import com.app.APICode.employee.message.UsernameMessage;
import com.app.APICode.user.User;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(summary = "List all Employees", description = "List all employees by the Restuarant that is owned by the User", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))), })
    @ResponseStatus(HttpStatus.OK)
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
    @Operation(summary = "Get Employee details", description = "Get employee details by username", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))), })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/employee")
    public Employee getEmployee(Principal principal) {
        return employeeService.getEmployeeByUsername(principal.getName());
    }

    /**
     * Add new employee with POST request to "/users/{username}/employee/{restaurantId}" 
     * If there is no user with the given username, throw a UserNotFoundException
     * 
     * @param username username of employee
     * @param restrauntId id of the restraunt the employee wants to apply to
     * @param designation Designation of the employee
     * @return the newly added employee
     */
    @Operation(summary = "Add User to Business", description = "Add a pending request for User to join Business", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful added request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))), })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/employee")
    public Employee postEmployee(Principal principal, @RequestBody RequestMessage message) {
        return employeeService.addEmployeeToBusiness(principal.getName(), message.getDesignation(),
                message.getRestaurantId());
    }

    /**
     * Change the Employee status to "Active" 
     * Change the User authorities to "ROLE_EMPLOYEE"
     * 
     * @param username username of employee
     * @return employee with the given username
     */
    @Operation(summary = "Approve Employee request", description = "Update request status of Employee", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful approved request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))), })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/employee")
    public Employee approveEmployee(@Valid @RequestBody UsernameMessage message) {
        return employeeService.approveEmployee(message.getUsername());
    }

    /**
     * Get user by username set its authorities to "ROLE_USER" get the employee set
     * the employee restraunt to null set the user employee to null
     * 
     * return the deleted user
     * 
     * @param username username of employee
     * @return deleted employee
     */
    @Operation(summary = "Delete Employee information", description = "Delete Employee information by the Owner or the User", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Employee" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful deleted Employee data", content = @Content), })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/employee/{username}")
    public void deleteEmployee(@PathVariable(value = "username") String username) {
        employeeService.deleteEmployee(username);
    }
}

package com.app.APICode.employee;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import com.app.APICode.employee.message.RequestMessage;
import com.app.APICode.employee.message.UsernameMessage;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;

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
	 * @return list of employees as {@link UserDTO} entities in a particular business
	 */
	@Operation(summary = "List all Employees", description = "List all employees by the Restuarant that is owned by the User", security = @SecurityRequirement(name = "bearerAuth"), tags = {
			"Employee" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))), })
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/employees")
	public List<UserDTO> getEmployees(Principal principal) {
		return employeeService.getAllEmployeesByBusinessOwner(principal.getName());
	}

	/**
	 * Search for {@link Employee} with the given username. 
	 * <p> If there is no {@link User} with the given username, throw a UserNotFoundException. 
	 * <p> If there is no {@link Employee} with the given username, throw a EmployeeNotFoundException
	 * 
	 * @param principal {@link Principal} object containing the username of the user logged in currently
	 * @return {@link Employee} object with the given username
	 */
	@Operation(summary = "Get Employee details", description = "Get employee details by username", security = @SecurityRequirement(name = "bearerAuth"), tags = {
			"Employee" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
			@ApiResponse(responseCode = "404", description = "Cannot find Employee with the following username", content = @Content), })
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/users/employee")
	public Employee getEmployee(Principal principal) {
		return employeeService.getEmployeeByUsername(principal.getName());
	}

	/**
	 * Add new employee with POST request. 
	 * <p> If there is no {@link User} with the given username, throw a UserNotFoundException.
	 * 
	 * @param {@link Principal} object containing the username of the user logged in currently
	 * @param message {@link RequestMessage} object containing the request for User to join a Business
	 * @return the newly added {@link Employee} object
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
	 * Change the {@link Employee} status to "Active" 
	 * <p> Change the {@link User} authorities to "ROLE_EMPLOYEE"
	 * 
	 * @param {@link Principal} object containing the username of the user logged in currently
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
	 * Get the {@link User} object by username and set its authorities to "ROLE_USER" 
	 * <p> Get the {@link Employee} object from the {@link User} and set
	 * <p> the restaurant attribute to null. Also sets the employee attribute to null
	 * 
	 * 
	 * @param username username of employee
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

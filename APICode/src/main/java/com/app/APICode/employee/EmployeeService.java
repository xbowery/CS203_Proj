package com.app.APICode.employee;

import java.util.List;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.restaurant.RestaurantNotFoundException;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;

public interface EmployeeService {
    /**
     * Gets all employees that work in the business with the specified business's
     * owner "username"
     * 
     * @param username a string containing the Business owner's username
     * @return a list of {@link UserDTO}
     */
    List<UserDTO> getAllEmployeesByBusinessOwner(String username);

    /**
     * Gets an Employee with the specified "username"
     * <p> If no Employee is found, throw a {@link EmployeeNotFoundException}.
     * 
     * @param username a string containing the username of the Employee
     * @return an {@link Employee} object
     */
    Employee getEmployeeByUsername(String username);

    /**
     * Gets an Employee with the specified "username", if the requester is authorised.
     * <p> If the requester is not a business owner, throw a {@link EmployeeNotAllowedException}.
     * 
     * @param requesterUsername a String containing the username of the requester
     * @param employeeUsername a string containing the username of the Employee
     * @return an {@link Employee} object
     */
    Employee getEmployeeDetailsByUsername(String requesterUsername, String employeeUsername);

    /**
     * Creates an Employee object for the user with specified "username" with the
     * "designation" working in a business specified by "businessId".
     * <p> If Restaurant is not found with associated "businessId", throw a {@link RestaurantNotFoundException}.
     * 
     * @param username     a string containing the username of the new Employee
     * @param designation a string containing the designation of the new Employee
     * @param businessId   a long containing the Restaurant ID
     * @return an {@link Employee} object
     */
    Employee addEmployeeToBusiness(String username, String designation, long businessId);

    /**
     * Updates the role of the User to an Employee when the Business Owner approves
     * the request.
     * 
     * @param username a string containing the username of the new Employee
     * @return an {@link Employee} object
     */
    Employee approveEmployee(String username);

    /**
     * Deletes the Employee object linked with the User specified by "Username".
     * Downgrades user's role to a normal User
     * <p>If User with associated "username" is not an Employee, throw a {@link EmployeeNotFoundException}.
     * 
     * @param username a string containing the username of the new Employee
     * 
     */
    void deleteEmployee(String username);
}

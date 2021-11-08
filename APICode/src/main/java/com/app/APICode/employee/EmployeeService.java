package com.app.APICode.employee;

import java.util.List;

import com.app.APICode.ctest.Ctest;
import com.app.APICode.user.User;

public interface EmployeeService {
    /**
     * Gets all employees that work in the business with the specified business's
     * owner "username"
     * 
     * @param username a string containing the Business owner's username
     * @return a list of User
     */
    List<User> getAllEmployeesByBusinessOwner(String username);

    /**
     * Gets an employee with the specified "username"
     * 
     * @param username a string containing the username of the Employee
     * @return an Employee object
     */
    Employee getEmployeeByUsername(String username);

    /**
     * Creates an Employee object for the user with specified "username" with the
     * "designation" working in a business specified by "businessId"
     * 
     * @param username     a string containing the username of the new Employee
     * @param designationa a string containing the designation of the new Employee
     * @param businessId   a long containing the Restaurant ID
     * @return an Employee object
     */
    Employee addEmployeeToBusiness(String username, String designation, long businessId);

    /**
     * Updates the role of the User to an Employee when the Business Owner approves
     * the request.
     * 
     * @param username a string containing the username of the new Employee
     * @return an Employee object
     */
    Employee approveEmployee(String username);

    /**
     * Deletes the Employee object linked with the User specified by "Username".
     * Downgrades user's role to a normal User
     * 
     * @param username a string containing the username of the new Employee
     * 
     */
    void deleteEmployee(String username);

    /**
     * Gets all {@link Ctest} for employees working in the business with the
     * specified business's owner "username"
     * 
     * @param username
     * @return
     */
    List<Ctest> getAllEmployeesCtest(String username);
}

package com.app.APICode.ctest;

import java.sql.Date;
import java.util.List;

import com.app.APICode.employee.EmployeeForbiddenException;
import com.app.APICode.employee.EmployeeNotFoundException;

public interface CtestService {
     /**
     * Retrieves a list of Ctest with the associated "username".
     * 
     * @param username 
     * @return a list of Ctest
     */
    List<Ctest> getAllCtestsByUsername(String username);

    /**
     * Save the Ctest with a given "username".
     * 
     * @param username 
     * @param ctest a Ctest object containing the new ctest info
     * @return the new Ctest object
     */
    Ctest saveCtestByUsername(String username, Ctest ctest);

    /**
     * Updates the Ctest with the given "username" and "ctestId". 
     * 
     * <p>If no Ctest is found, throw
     * a {@link CtestNotFoundException}
     * 
     * @param username a string containing the username of the User
     * @param ctestId
     * @param newCtest Ctest object containing the new Ctest
     * @return an updated Ctest object
     */
    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    /**
     * Deletes the Ctest with the given "username" and "ctestId". 
     * <p>If no Ctest is found, throw
     * a {@link CtestNotFoundException}
     * 
     * @param username a string containing the username of the User
     * @param ctestId
     */
    void deleteCtestByCtestIdAndUsername(String username, Long ctestId);

    /**
     * Retrieves the date of next Ctest with the associated "username".
     * 
     * @param username a string containing the username of the User
     * @return Date of next Ctest
     */
    Date getNextCtestByUsername(String username);

     /**
     * Retrieves a list of employees' Ctest with the associated "username", belonging to restaurant owner.
     *<p>If User(restaurant owner) is not found, throw a {@link EmployeeNotFoundException}.
     * <p>If given username is not a business owner, throw a {@link EmployeeForbiddenException}.
     * 
     * @param username restuarant owner's username
     * @return a list of all employees' Ctest
     */
    List<Ctest> getAllEmployeesCtest(String username);
}

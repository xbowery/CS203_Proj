package com.app.APICode.ctest;

import java.sql.Date;
import java.util.List;

import com.app.APICode.employee.EmployeeForbiddenException;
import com.app.APICode.employee.EmployeeNotFoundException;

/**
 * Service interface layer that performs CRUD operations for {@link Ctest}
 */
public interface CtestService {
     /**
     * Retrieves a list of Ctest with the associated "username".
     * 
     * @param username 
     * @return a list of {@link Ctest}
     */
    List<Ctest> getAllCtestsByUsername(String username);

    /**
     * Save the Ctest with a given "username".
     * 
     * @param username 
     * @param ctest a Ctest object containing the new ctest info
     * @return the new {@link Ctest} object
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
     * @return an updated {@link Ctest} object
     */
    Ctest updateCtestByCtestIdAndUsername(String username, Long ctestId, Ctest newCtest);

    /**
     * Deletes the {@link Ctest} with the given "username" and "ctestId". 
     * <p>If no {@link Ctest} is found, throw
     * a {@link CtestNotFoundException}
     * 
     * @param username a string containing the username of the User
     * @param ctestId
     */
    void deleteCtestByCtestIdAndUsername(String username, Long ctestId);

    /**
     * Retrieves the date of next {@link Ctest} with the associated "username".
     * 
     * @param username a string containing the username of the User
     * @return Date of next {@link Ctest}
     */
    Date getNextCtestByUsername(String username);

     /**
     * Retrieves a list of employees' {@link Ctest} with the associated "username", belonging to restaurant owner.
     * <p>If User (Business Owner) is not found, throw a {@link EmployeeNotFoundException}.
     * <p>If given username is not a Business Owner, throw a {@link EmployeeForbiddenException}.
     * 
     * @param username restuarant owner's username
     * @return a list of all employees' {@link Ctest}
     */
    List<Ctest> getAllEmployeesCtest(String username);
}

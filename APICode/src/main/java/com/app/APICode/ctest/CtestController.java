package com.app.APICode.ctest;

import java.util.List;
import java.util.Optional;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.employee.EmployeeRepository;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserRepository;
import com.app.APICode.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CtestController {
    private CtestService ctests;
    private EmployeeRepository employees;
    private UserService users;

    public CtestController(CtestService ctests, EmployeeRepository employees, UserService users) {
        this.ctests = ctests;
        this.employees = employees;
        this.users = users;
    }
    
    /**
     * Search for employee with the given username
     * If there is no user with the given username, throw a UserNotFoundException
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return list of the ctests done by the employee
     */
    @GetMapping("/employee/{username}/ctests")
    public List<Ctest> getAllTestsByEmployee(@PathVariable (value = "username") String username) {
        User user = users.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        return ctests.getAllCtestsByEmployee(employee);
    }

    /**
     * Add a new ctest with POST request to "/employee/{username}/ctests"
     * 
     * @param username username of employee
     * @param ctest a new ctest object to be added
     * @return the newly added ctest object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/employee/{username}/ctests")
    public Ctest addCtest(@PathVariable (value = "username") String username, @RequestBody Ctest ctest){
        User user = users.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        ctest.setEmployee(employee);
        return ctests.saveCtest(ctest);
    }

    /**
     * Updates the info an employee's ctest
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @param ctestId a long value (Ctest)
     * @param newCtest a Ctest object containing the new Ctest info to be updated
     * @return the updated Ctest object
     */
    @PutMapping("/employee/{username}/ctests/{ctestId}")
    public Ctest updateCtest(@PathVariable (value = "username") String username, @PathVariable(value = "ctestId") Long ctestId, @RequestBody Ctest newCtest){
        User user = users.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        return ctests.updateCtestByCtestIdAndEmployeeId(ctestId, employee.getId(), newCtest);
    }

    /**
     * Remove a Ctest with the DELETE request to "/employee/{username}/ctests/{ctestId}"
     * If there is no user with the given "username" throw a UserNotFoundException
     * If there is no employee with the user, throw a EmployeeNotFoundException
     * If there is no Ctest with the given "id", throw a CtestNotFoundException
     * 
     * @param username username of employee
     * @param ctestId a long value (Ctest)
     */
    @DeleteMapping("/employee/{username}/ctests/{ctestId}")
    public Ctest deleteCtest(@PathVariable (value = "username") String username, @PathVariable (value = "ctestId") Long ctestId){
        User user = users.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        return ctests.deleteCtestByCtestIdAndEmployeeId(ctestId, employee.getId());
    }
}

package com.app.APICode.ctest;

import java.util.List;
import java.util.Optional;

import com.app.APICode.employee.Employee;
import com.app.APICode.employee.EmployeeNotFoundException;
import com.app.APICode.employee.EmployeeRepository;
import com.app.APICode.user.User;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserRepository;

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
    private CtestRepository ctests;
    private EmployeeRepository employees;
    private UserRepository users;

    public CtestController(CtestRepository ctests, EmployeeRepository employees, UserRepository users){
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
    public List<Ctest> getAllTestsByEmployeeId(@PathVariable (value = "username") String username) {
        Optional<User> user = users.findByUsername(username);
        if(!user.isPresent()){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.get().getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        return ctests.findByEmployee(employee);
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
        Optional<User> user = users.findByUsername(username);
        if(!user.isPresent()){
            throw new UserNotFoundException(username);
        }
        Employee employee = user.get().getEmployee();
        if(employee == null){
            throw new EmployeeNotFoundException(username);
        }
        ctest.setEmployee(employee);
        return ctests.save(ctest);
    }

    /**
     * Updates the info an employee's ctest
     * If there is no employee with the given username, throw a EmployeeNotFoundException
     * 
     * @param id a long value (Employee)
     * @param ctestId a long value (Ctest)
     * @param newCtest a Ctest object containing the new Ctest info to be updated
     * @return the updated Ctest object
     */
    @PutMapping("/employee/{employee_id}/ctests/{ctestId}")
    public Ctest updateCtest(@PathVariable (value = "employee_id") Long id, @PathVariable(value = "ctestId") Long ctestId, @RequestBody Ctest newCtest){
        if(!employees.findById(id).isPresent()) {
            throw new EmployeeNotFoundException(id);
        }
        return ctests.findByIdAndEmployeeId(id, ctestId).map(ctest -> {
            ctest.setType(newCtest.getType());
            ctest.setDate(newCtest.getDate());
            ctest.setResult(newCtest.getResult());
            return ctests.save(ctest);
        }).orElseThrow(() -> new CtestNotFoundException(id));
    }

    /**
     * Remove a Ctest with the DELETE request to "/employee/{employee_id}/ctests/{ctestId}"
     * If there is no employee with the given "id", throw a EmployeeNotFoundException
     * If there is no Ctest with the given "id", throw a CtestNotFoundException
     * 
     * @param employeeId a long value (Employee)
     * @param ctestId a long value (Ctest)
     * @return ResponseEntity object
     */
    @DeleteMapping("/employee/{employee_id}/ctests/{ctestId}")
    public ResponseEntity<?> deleteCtest(@PathVariable (value = "employee_id") Long employeeId, @PathVariable (value = "ctestId") Long ctestId){
        if(!employees.findById(employeeId).isPresent()) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return ctests.findByIdAndEmployeeId(ctestId, employeeId).map(ctest -> {
            ctests.delete(ctest);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new CtestNotFoundException(ctestId));
    }
}

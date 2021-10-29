package com.app.APICode.ctest;

import java.security.Principal;
import java.util.List;

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
public class CtestController {
    private CtestService ctests;

    public CtestController(CtestService ctests) {
        this.ctests = ctests;
    }

    /**
     * Search for employee with the given username If there is no user with the
     * given username, throw a UserNotFoundException If there is no employee with
     * the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return list of the ctests done by the employee
     */
    @GetMapping("/employee/ctests")
    public List<Ctest> getAllTestsByEmployee(Principal principal) {
        return ctests.getAllCtestsByUsername(principal.getName());
    }

    /**
     * Add a new ctest with POST request to "/employee/ctests"
     * 
     * @param username username of employee
     * @param ctest    a new ctest object to be added
     * @return the newly added ctest object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/employee/ctests")
    public Ctest addCtest(Principal principal, @RequestBody Ctest ctest) {
        return ctests.saveCtestByUsername(principal.getName(), ctest);
    }

    /**
     * Updates the info an employee's ctest If there is no employee with the given
     * username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @param ctestId  a long value (Ctest)
     * @param newCtest a Ctest object containing the new Ctest info to be updated
     * @return the updated Ctest object
     */
    @PutMapping("/employee/ctests/{ctestId}")
    public Ctest updateCtest(Principal principal, @PathVariable(value = "ctestId") Long ctestId,
            @RequestBody Ctest newCtest) {
        return ctests.updateCtestByCtestIdAndUsername(principal.getName(), ctestId, newCtest);
    }

    /**
     * Remove a Ctest with the DELETE request to
     * "/employee/{username}/ctests/{ctestId}" If there is no user with the given
     * "username" throw a UserNotFoundException If there is no employee with the
     * user, throw a EmployeeNotFoundException If there is no Ctest with the given
     * "id", throw a CtestNotFoundException
     * 
     * @param username username of employee
     * @param ctestId  a long value (Ctest)
     */
    @DeleteMapping("/employee/ctests/{ctestId}")
    public Ctest deleteCtest(Principal principal, @PathVariable(value = "ctestId") Long ctestId) {
        return ctests.deleteCtestByCtestIdAndUsername(principal.getName(), ctestId);
    }
}

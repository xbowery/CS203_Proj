package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.employee.EmployeeRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CtestController {
    private CtestRepository ctests;
    private EmployeeRepository employees;

    public CtestController(CtestRepository ctests, EmployeeRepository employees){
        this.ctests = ctests;
        this.employees = employees;
    }

    @GetMapping("/ctests")
    public List<Ctest> getCtest(){
        return ctests.findAll();
    }
    
    // @GetMapping("/user/{userid}/ctests")
    // public List<Ctest> getAllReviewsByUserName(@PathVariable (value = "userId") String username) {
    //     if(!users.findByUsername(username).isPresent()) {
    //         throw new UserNotFoundException(username);
    //     }
    //     return ctests.findByEmployee(users.findByUsername(username).get().getEmployee());
    // }
}

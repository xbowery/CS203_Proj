package com.app.APICode.ctest;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import com.app.APICode.user.*;

@RestController
public class CtestController {
    private CtestRepository ctests;
    private UserRepository users;

    public CtestController(CtestRepository ctests, UserRepository users){
        this.ctests = ctests;
        this.users = users;
    }

    @GetMapping("/ctests")
    public List<Ctest> getCtest(){
        return ctests.findAll();
    }
    @GetMapping("/user/{userid}/ctests")
    public List<Ctest> getAllReviewsByUserName(@PathVariable (value = "userId") String username) {
        if(!users.findByUsername(username).isPresent()) {
            throw new UserNotFoundException(username);
        }
        return ctests.findByEmployee(users.findByUsername(username).get().getEmployee());
    }

    /*
     * add a ctest for a given userId
     * If there's no such user, throw a UserNotFoundException
     * 
     * Return the newly added ctest
     */
    @PostMapping("/user/{email}/ctests")
    public Ctest addCtest(@PathVariable (value = "email") String username, @RequestBody Ctest ctest){
        return users.findByUsername(username).map(user ->{
            ctest.setEmployee(user.getEmployee());
            return ctests.save(ctest);
        }).orElseThrow(() -> new UserNotFoundException(username));
    }
}

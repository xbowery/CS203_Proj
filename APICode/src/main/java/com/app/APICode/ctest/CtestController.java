package com.app.APICode.ctest;

import java.util.List;

import com.app.APICode.user.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    // @GetMapping("/user/{userid}/ctests")
    // public List<Ctest> getAllReviewsByUserId(@PathVariable (value = "userId") Long userId) {
    //     if(!users.existsById(userId)) {
    //         throw new UserNotFoundException(userId);
    //     }
    //     return ctests.findByUserId(userId);
    // }
}

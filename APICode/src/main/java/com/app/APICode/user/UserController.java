package com.app.APICode.user;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    public UserController(UserService userService, BCryptPasswordEncoder encoder){
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    // need to do the mapping properly
    @GetMapping("/users/{email}")
    public User getUser(@PathVariable String email) {
        User user = userService.getUser(email);

        if (user == null) throw new UserNotFoundException(email);
        return userService.getUser(email);
    }

    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * @param user
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userService.addUser(user);
        if (savedUser == null) throw new UserExistsException(user.getEmail());
        return savedUser;
    }

    /**
     * If there is no user with the given email, throw a UserNotFoundException
     * @param email
     * @param newUserInfo
     * @return the updated user
     */
    @PutMapping("/users/{email}")
    public User updateUser(@PathVariable String email, @Valid @RequestBody User newUserInfo) {
        User user = userService.updateUser(email, newUserInfo);
        if (user == null) throw new UserNotFoundException(email);

        return user;
    }

    @Transactional
    @DeleteMapping("/users/{email}")
    public void deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(email);
        }
    }
}


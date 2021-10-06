package com.app.APICode.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.app.APICode.security.jwt.JWTRefreshToken;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    private JWTRefreshToken refreshToken;

    public UserController(UserService userService, BCryptPasswordEncoder encoder, JWTRefreshToken refreshToken){
        this.userService = userService;
        this.encoder = encoder;
        this.refreshToken = refreshToken;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    // need to do the mapping properly
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        User user = userService.getUser(username);

        if (user == null) throw new UserNotFoundException(username);
        return userService.getUser(username);
    }

    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * @param user
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
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
    @PutMapping("/users/{username}")
    public User updateUser(@PathVariable String username, @Valid @RequestBody User newUserInfo) {
        User user = userService.updateUserByUsername(username, newUserInfo);
        if (user == null) throw new UserNotFoundException(username);

        return user;
    }

    @Transactional
    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(username);
        }
    }

    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException{
        refreshToken.refreshJwtToken(req, res);
    }

}

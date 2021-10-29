package com.app.APICode.user;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.app.APICode.security.jwt.JWTRefreshToken;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private JWTRefreshToken refreshToken;

    /**
     * Constructor method for UserController
     * 
     * @param userService  UserService class
     * @param encoder      BCryptPasswordEncoder class
     * @param refreshToken JWTRefreshToken class
     */
    public UserController(UserService userService, JWTRefreshToken refreshToken) {
        this.userService = userService;
        this.refreshToken = refreshToken;
    }

    /**
     * List all users in the database
     * 
     * @return list of all users
     */
    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.listUsers();
    }

    /**
     * Search for user with the given username If there is no user with the given
     * username, throw a UserNotFoundException
     * 
     * @param username
     * @return user with the given username
     */
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Add a new user with POST request to "/users" Using BCrypt encoder to encrypt
     * the password for storage
     * 
     * @param user a User object containing the user information to be added
     * @return user with the admin role
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDTO addUser(@Valid @RequestBody User user) {
        Boolean isAdmin = true;
        return userService.addUser(user, isAdmin);
    }

    /**
     * Search for user given the username and updates the relevant user details If
     * there is no user with the given username, throw a UserNotFoundException
     * 
     * @param username    username of user
     * @param newUserInfo a User object containing the new user info to be updated
     * @return the updated User object
     */
    @Transactional
    @PutMapping("/users")
    public UserDTO updateUser(Principal principal, @Valid @RequestBody UserDTO newUserInfo) {
        return userService.updateUserByUsername(principal.getName(), newUserInfo);
    }

    /**
     * Remove a user with the DELETE request to "/users/{username}" If there is no
     * user with the given username, throw a UserNotFoundException
     * 
     * @param username username of user
     */
    @Transactional
    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    /**
     * Refreshes the JWT token
     * 
     * @param req a HttpServletRequest object
     * @param res a HttpServletResponse cookie
     * @throws IOException
     */
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        refreshToken.refreshJwtToken(req, res);
    }

    /**
     * Retrieves the email from the request parameter and finds the user Resets the
     * password for the user If there is no user with the given email, throw a
     * UserNotFoundException
     * 
     * @param payload the request parameter
     */
    @PostMapping("/forgotPassword")
    public void resetPassword(@RequestBody Map<String, String> payload) {
        String email = "";
        try {
            email = payload.get("email");
        } catch (Exception e) {
            throw new RuntimeException("Invalid request");
        }
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }
        userService.createTempPassword(email);
    }

    /**
     * Retrieves the new user information and proceed to save the user into the
     * database If there is already an existing user with the given email or
     * username, throw a UserOrEmailExistsException
     * 
     * @param newUser a User object containing the new user info to be saved
     * @return the newly created user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody User newUser) {
        Boolean isAdmin = false;
        return userService.addUser(newUser, isAdmin);
    }

    /**
     * Function to call to confirm a user's registration
     * 
     * @param token
     * @return
     */
    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") final String token) {
        return userService.validateVerificationToken(token);
    }
}

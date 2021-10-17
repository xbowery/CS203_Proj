package com.app.APICode.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private BCryptPasswordEncoder encoder;
    private JWTRefreshToken refreshToken;

    /**
     * Constructor method for UserController
     * 
     * @param userService UserService class
     * @param encoder BCryptPasswordEncoder class
     * @param refreshToken JWTRefreshToken class
     */
    public UserController(UserService userService, BCryptPasswordEncoder encoder, JWTRefreshToken refreshToken) {
        this.userService = userService;
        this.encoder = encoder;
        this.refreshToken = refreshToken;
    }

    /**
     * Gets all the users stored in the database
     * 
     * @return list of all users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    /**
     * Finds the username of the User and returns the User page
     * 
     * @param username Username of the user
     * @return the user
     */
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);
        return userService.getUserByUsername(username);
    }

    /**
     * Using BCrypt encoder to encrypt the password before sending the User back into the
     * database to persist the user
     * 
     * @param user User entity 
     * @return saved user is successful; null if unsuccessful
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        Boolean isAdmin = true;
        User savedUser = userService.addUser(user, isAdmin);
        return savedUser;
    }

    /**
     * If there is no user with the given username, throw a UserNotFoundException
     * 
     * @param email
     * @param newUserInfo
     * @return the updated user
     */
    @PutMapping("/users/{username}")
    public User updateUser(@PathVariable String username, @Valid @RequestBody User newUserInfo) {
        User user = userService.updateUserByUsername(username, newUserInfo);
        if (user == null)
            throw new UserNotFoundException(username);

        return user;
    }

    /**
     * Deletes the user if there exists a user
     * else catch the exception and throw a UserNotFoundException 
     * to inform that the user does not exist
     * 
     * @param username Username of the user
     */
    @Transactional
    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Refreshes the JWT token
     * 
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @throws IOException
     */
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        refreshToken.refreshJwtToken(req, res);
    }

    /**
     * Function to call for forgot password
     * 
     * @param req
     * @param userInput
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
     * Function to call for registering a new user
     * 
     * @param newUser
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@Valid @RequestBody User newUser) {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        Boolean isAdmin = false;
        final User savedUser = userService.addUser(newUser, isAdmin);
        if (savedUser == null)
            throw new UserOrEmailExistsException(newUser.getUsername());
        return savedUser;
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

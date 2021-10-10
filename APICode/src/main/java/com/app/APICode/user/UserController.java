package com.app.APICode.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.app.APICode.security.jwt.JWTRefreshToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    private JWTRefreshToken refreshToken;
    private VerificationTokenRepository vTokens;

    public UserController(UserService userService, BCryptPasswordEncoder encoder, JWTRefreshToken refreshToken, VerificationTokenRepository vTokens) {
        this.userService = userService;
        this.encoder = encoder;
        this.refreshToken = refreshToken;
        this.vTokens = vTokens;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    // need to do the mapping properly
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException(username);
        return userService.getUserByUsername(username);
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * 
     * @param user
     * @return
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
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        refreshToken.refreshJwtToken(req, res);
    }

    @PostMapping("/forgotPassword")
    public void resetPassword(HttpServletRequest req, @ModelAttribute(name = "user") User userInput) {
        User user = userService.getUserByEmail(userInput.getEmail());
        if (user == null) {
            throw new UserNotFoundException(userInput.getEmail());
        }
        userService.createTempPassword(userInput.getEmail());
    }

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

}

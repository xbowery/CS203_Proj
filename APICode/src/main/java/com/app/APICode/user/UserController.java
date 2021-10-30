package com.app.APICode.user;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User", description = "User API")
public class UserController {
    private UserService userService;

    /**
     * Constructor method for UserController
     * 
     * @param userService UserService class
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * List all users in the database
     * 
     * @return list of all users
     */
    @Operation(summary = "List all Users", security = @SecurityRequirement(name = "bearerAuth"), tags = { "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))), })
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
    @Operation(summary = "Get User", description = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval of User", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))) })
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
    @Operation(summary = "Add new user", security = @SecurityRequirement(name = "bearerAuth"), tags = { "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful created new User", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), })
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
    @Operation(summary = "Update user information", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "User" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful updated User information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))), })
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
    @Operation(summary = "Delete User", security = @SecurityRequirement(name = "bearerAuth"), tags = { "User" })
    @ApiResponses({ @ApiResponse(responseCode = "204", description = "Successful deleted User", content = @Content) })
    @Transactional
    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
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
    @Operation(summary = "Register User", tags = { "User" })
    @ApiResponses({ @ApiResponse(responseCode = "204", description = "Successful Registered User") })
    @ResponseStatus(HttpStatus.NO_CONTENT)
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

package com.app.APICode.user;

import java.util.List;

import com.app.APICode.user.message.ChangePasswordMessage;

public interface UserService {
    /**
     * Retrieves all users registered but converted to the UserDTO entity.
     * 
     * @return a list of {@link UserDTO}
     */
    List<UserDTO> listUsers();

    /**
     * Retrieves all users registered.
     * 
     * @return a list of {@link User}
     */
    List<User> getAllUsers();

    /**
     * Gets the User details by the specified "username". 
     * <p>If the principal does not
     * match the specified "username", throw a {@link UserForbiddenException}.
     * <p> If no User is found, throw a {@link UserNotFoundException}.
     * 
     * @param requesterUsername the username for verification
     * @param username          a String containing the username to be retrieved
     * @return a {@link UserDTO} object
     */
    UserDTO getUserDetailsByUsername(String requesterUsername, String username);

    /**
     * Gets the User associated with the given "username".
     * <p>If no User is found, throw a
     * {@link UserNotFoundException}.
     * 
     * @param username a String containing the username
     * @return a {@link User} object
     */
    User getUserByUsername(String username);

    /**
     * Gets the User associated with the given "email". <p> If no User found, throw a
     * {@link EmailNotFoundException}.
     * 
     * @param email a String containing the email
     * @return a {@link User} object
     */
    User getUserByEmail(String email);
    
    /**
     * Creates a new user with the given "user" and "isAdmin" to determine the role
     * of the new user. <p>If new email or username exists, throw a
     * {@link UserOrEmailExistsException}.
     * 
     * @param user    a User object
     * @param isAdmin boolean value to determine if user is admin or not
     * @return the newly added {@link User} object
     */
    UserDTO addUser(User user, Boolean isAdmin);

    /**
     * Updates the User info with the given "username". <p>If new email or username
     * exists, throw a {@link UserOrEmailExistsException}.
     * 
     * @param username a string containing the username of the User
     * @param user     a {@link UserDTO} object containning the new info to be updated
     * @return an updated {@link UserDTO}
     */
    void updateUserByUsername(String username, UserDTO user);

    /**
     * Updates the User password with the given "email". <p>If no User is found, throw
     * a {@link UserNotFoundException}.
     * 
     * @param email    a string containing the email of the User
     * @param password a string containing the plaintext password
     * @return an updated {@link User} object
     */
    User updatePasswordByEmail(String email, String password);

    /**
     * Generates a new password for the user with the given "email". <p> If no user is
     * found with associated "email", throw a {@link NoContentResponse}.
     * 
     * @param email a string containing the email of the {@link User}
     * 
     */
    void createTempPassword(String email);

    /**
     * Deletes the user with the given "username". <p>If user does not exists, throw a
     * {@link UserNotFoundException}.
     * 
     * @param username a String containing User's username
     */
    void deleteUser(String username);

    /**
     * Save/update the given "user".
     * 
     * @param user a {@link User} object containing the new/updated user info
     * @return the new/updated {@link User} object
     */
    User save(User user);

    /**
     * Updates the password of the user specified by "username" with password
     * details specfied in "message". <p>If password in User object does not match with
     * the current password specified in message, throw a
     * {@link InvalidChangePasswordException}. <p>If the new password and confirm new password
     * does not match, throw a {@link InvalidChangePasswordException}.
     * 
     * @param username a string containing the username of the User
     * @param message  a ChangePasswordMessage object containing the current
     *                 password, new password and confirm new password
     */
    void changePasswordByUsername(String username, ChangePasswordMessage message);
}

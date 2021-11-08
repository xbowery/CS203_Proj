package com.app.APICode.user;

import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.passwordresettoken.PasswordResetToken;

import java.util.List;

public interface UserService {
    /**
     * Retrieves all users registered
     * 
     * @return a list of UserDTO
     */
    List<UserDTO> listUsers();

    /**
     * Gets the User details by the specified "username".
     * If the principal does not match the specified "username", throw a {@link UserForbiddenException}
     * @param requesterUsername the username for verification
     * @param username a String containing the username to be retrieved
     * @return
     */
    UserDTO getUserDetailsByUsername(String requesterUsername, String username);

    /**
     * Gets the User associated with the given "username" If no User found, throw a
     * {@link UserNotFoundException}
     * 
     * @param username a String containing the username
     * @return an User object
     */
    User getUserByUsername(String username);

    /**
     * Gets the User associated with the given "email" If no User found, throw a
     * {@link UserNotFoundException}
     * 
     * @param email a String containing the email
     * @return an User object
     */
    User getUserByEmail(String email);

    /**
     * Gets the VerificationToken object specified by the given "verificationToken"
     * 
     * @param VerificationToken a String containing the token
     * @return a VerificationToken object
     */
    VerificationToken getVerificationToken(String verificationToken);

    /**
     * Saves a VerificationToken for the specified "user" with the specified
     * "token".
     * 
     * @param user  a User Object
     * @param token a string containing the token
     */
    void createVerificationTokenForUser(User user, String token);

    /**
     * Updates an existing VerificationToken specified by
     * "existingVerificationToken" with a new token
     * 
     * @param existingVerificationToken
     * @return
     */
    VerificationToken generateNewVerificationToken(String existingVerificationToken);

    /**
     * Validates and return the status of the VerificationToken specified by "token"
     * 
     * @param token
     * @return a string of the status of the VerificationToken
     */
    String validateVerificationToken(String token);

    /**
     * Creates a PasswordReset entry for the specified "User" with the specified
     * "token".
     * 
     * @param user  an User Object
     * @param token a String containing the token for Password Reset
     */
    void createPasswordResetTokenForUser(User user, String token);

    /**
     * Gets the PasswordResetToken specified by the "token"
     * 
     * @param token a String containing the token.
     * @return a PasswordResetToken object
     */
    PasswordResetToken getPasswordResetToken(String token);

    /**
     * Get the User associated the the specified Password Reset "token"
     * 
     * @param token a String containing the token for Password Reset
     * @return a User Object
     */
    User getUserByPasswordResetToken(String token);

    /**
     * Creates a new user with the given "user" and "isAdmin" to determine the role
     * of the new user. If new email or username exists, throw a
     * {@link UserOrEmailExistsException}
     * 
     * @param user    a User object
     * @param isAdmin boolean value to determine if user is admin or not
     * @return the newly added User object
     */
    UserDTO addUser(User user, Boolean isAdmin);

    /**
     * Updates the User info with the given "username". If new email or username
     * exists, throw a {@link UserOrEmailExistsException}
     * 
     * @param username a string containing the username of the User
     * @param user     a UserDTO object containning the new info to be updated
     * @return an updated UserDTO
     */
    UserDTO updateUserByUsername(String username, UserDTO user);

    /**
     * Updates the User password with the given "email". If no User is found, throw
     * a {@link UserNotFoundException}
     * 
     * @param email    a string containing the email of the User
     * @param password a string containing the plaintext password
     * @return an updated User object
     */
    User updatePasswordByEmail(String email, String password);

    /**
     * Generates a new password for the user with the given "email" If no user is
     * found, throw a {@link EmailNotFoundException}
     * 
     * @param email a string containing the email of the User
     * 
     */
    void createTempPassword(String email);

    /**
     * Deletes the user with the given "username". If user does not exists, throw a
     * {@link UserNotFoundException}
     * 
     * @param username
     */
    void deleteUser(String username);

    /**
     * Save/update the given "user"
     * 
     * @param user a User object containing the new/updated user info
     * @return the new/updated User object
     */
    User save(User user);
}

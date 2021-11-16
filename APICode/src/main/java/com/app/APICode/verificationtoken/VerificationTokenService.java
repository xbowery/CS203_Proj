package com.app.APICode.verificationtoken;

import com.app.APICode.user.User;

/**
 * Service interface layer that performs CRUD operations for {@link VerificationToken}
 */
public interface VerificationTokenService {
    /**
     * Gets the {@link VerificationToken} object specified by the given "verificationToken".
     * 
     * @param verificationToken a String containing the token
     * @return a {@link VerificationToken} object
     */
    VerificationToken getVerificationToken(String verificationToken);

    /**
     * Saves a VerificationToken for the specified "user" with the specified
     * "token".
     * 
     * @param user  a {@link User} Object
     * @param token a string containing the token
     */
    VerificationToken createVerificationTokenForUser(User user, String token);

    /**
     * Updates an existing VerificationToken specified by
     * "existingVerificationToken" with a new token.
     * 
     * @param existingVerificationToken
     * @return a newly generated {@link VerificationToken}
     */
    VerificationToken generateNewVerificationToken(String existingVerificationToken);

    /**
     * Validates and return the status of the VerificationToken specified by "token".
     * 
     * @param token a String containing the {@link VerificationToken}
     * @return a string of the status of the {@link VerificationToken}
     */
    String validateVerificationToken(String token);

}

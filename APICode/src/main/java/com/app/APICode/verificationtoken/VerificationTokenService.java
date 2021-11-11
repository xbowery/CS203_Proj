package com.app.APICode.verificationtoken;

import com.app.APICode.user.User;

public interface VerificationTokenService {
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
    VerificationToken createVerificationTokenForUser(User user, String token);

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

}

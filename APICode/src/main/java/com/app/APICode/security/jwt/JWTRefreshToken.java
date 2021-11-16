package com.app.APICode.security.jwt;

import com.app.APICode.security.exception.InvalidJwtException;
import com.app.APICode.security.message.AccessTokenMessage;
import com.app.APICode.user.CustomUserDetailsService;
import com.app.APICode.user.User;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class will help to automatically refresh and generate a new JWT 
 * for a user should his/her initial JWT be expired
 */
@Component
public class JWTRefreshToken {

    @Autowired
    CustomUserDetailsService userService;

    Logger logger = LoggerFactory.getLogger(JWTRefreshToken.class);

    public AccessTokenMessage refreshJwtToken(String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidJwtException();
        }
        try {
            DecodedJWT decodedJWT = JWTHelper.decodeJwt(refreshToken);

            // Check if it is an accessToken, since only access tokens have their roles
            if (!decodedJWT.getClaim("type").asString().equals("refresh")) {
                throw new InvalidJwtException();
            }

            String username = decodedJWT.getSubject();
            User user = (User) userService.loadUserByUsername(username);

            // Checks if the user is eligible for a new accessToken by checking if account
            // is still enabled
            if (!user.isEnabled()) {
                throw new RuntimeException("user " + username + " account is disabled");
            }

            return new AccessTokenMessage(user);

        } catch (JWTVerificationException e) {
            logger.info(e.getMessage());
            throw new InvalidJwtException();
        }
    }
}

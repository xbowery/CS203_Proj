package com.app.APICode.security.jwt;

import static com.app.APICode.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.app.APICode.security.jwt.SecurityConstants.ISSUER;
import static com.app.APICode.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;
import static com.app.APICode.security.jwt.SecurityConstants.SECRET;

import java.util.Date;
import java.util.stream.Collectors;

import com.app.APICode.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.core.GrantedAuthority;

/**
 * Helper class for JWT
 */
public class JWTHelper {
    /**
     * Generates a JWT Access Token
     * 
     * @param user used to populate the JWT subject and claims
     * @return a string containing the JWT
     */
    public static String generateAccessToken(User user) {
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).withIssuer(ISSUER)
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("type", "access").sign(getSigningAlgorithm());
    }

    /**
     * Generates a JWT Refresh Token. This token will be used to create new access
     * token when it expires.
     * 
     * @param user used to populate the JWT subject
     * @return a string containing the JWT
     */
    public static String generateRefreshToken(User user) {
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME)).withIssuer(ISSUER)
                .withClaim("type", "refresh").sign(getSigningAlgorithm());
    }

    /**
     * Decodes JWT for data access
     * 
     * @param token String containing the JWT
     * @return a {@link DecodedJWT}
     */
    public static DecodedJWT decodeJwt(String token) {
        return JWT.require(getSigningAlgorithm()).build().verify(token);
    }

    /**
     * Get the Algorithm to sign JWT
     * 
     * @return a {@link Algorithm}
     */
    public static Algorithm getSigningAlgorithm() {
        return Algorithm.HMAC512(SECRET.getBytes());
    }
}

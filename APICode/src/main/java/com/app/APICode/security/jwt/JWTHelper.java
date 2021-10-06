package com.app.APICode.security.jwt;

import static com.app.APICode.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.app.APICode.security.jwt.SecurityConstants.REFRESH_EXPIRATION_TIME;
import static com.app.APICode.security.jwt.SecurityConstants.SECRET;

import java.util.Date;
import java.util.stream.Collectors;

import com.app.APICode.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JWTHelper {

    /**
     * Generates a JWT Access Token
     * 
     * @param user used to populate the JWT subject and claims
     * @param issuer to identify the issuer of the JWT
     * @return a string containing the JWT
     */
    public String generateAccessToken(User user, String issuer) {
        return JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withIssuer(issuer)
            .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(getSigningAlgorithm());
    }

    /**
     * Generates a JWT Refresh Token
     * 
     * @param user used to populate the JWT subject
     * @param issuer to identify the issuer of the JWT
     * @return a string containing the JWT
     */
    public String generateRefreshToken(User user, String issuer) {
        return JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
            .withIssuer(issuer)
            .sign(getSigningAlgorithm());
    }

    /**
     * Decodes JWT for data access
     * @param token String containing the JWT
     * @return a {@link DecodedJWT}
     */
    public DecodedJWT decodeJwt(String token) {
        return JWT.require(getSigningAlgorithm()).build().verify(token);
    }

    /**
     * Get the Algorithm to sign JWT
     * @return a {@link Algorithm}
     */
    public Algorithm getSigningAlgorithm() {
        return Algorithm.HMAC512(SECRET.getBytes());
    }
}

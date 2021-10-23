package com.app.APICode.security.jwt;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.user.CustomUserDetailsService;
import com.app.APICode.user.User;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTRefreshToken {

    @Autowired
    CustomUserDetailsService userService;

    @Autowired
    JWTHelper jwtHelper;

    Logger logger = LoggerFactory.getLogger(JWTRefreshToken.class);

    public void refreshJwtToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        Map<String, String> tokensMap = new ObjectMapper().readValue(req.getInputStream(), typeRef);
        String refreshToken = tokensMap.get("refreshToken");

        try {
            if (refreshToken == null) {
                throw new RuntimeException("No JWT Refresh Token provided");
            }

            DecodedJWT decodedJWT = jwtHelper.decodeJwt(refreshToken);

            // Check if it is an accessToken, since only access tokens have their roles
            if (!decodedJWT.getClaim("type").asString().equals("refresh")) {
                throw new RuntimeException("Incorrect token type");
            }

            String username = decodedJWT.getSubject();

            User user = (User) userService.loadUserByUsername(username);

            // Checks if the user is eligible for a new accessToken by checking if account
            // is still enabled
            if (!user.isEnabled()) {
                throw new RuntimeException("user " + username + " account is disabled");
            }

            String accessToken = jwtHelper.generateAccessToken(user);

            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType(APPLICATION_JSON_VALUE);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("username", user.getUsername());

            new ObjectMapper().writeValue(res.getOutputStream(), tokens);

        } catch (Exception e) {
            logger.info(e.getMessage());
            res.setContentType(APPLICATION_JSON_VALUE);
            res.setStatus(FORBIDDEN.value());

            Map<String, String> error = new HashMap<>();
            error.put("error_message", "JWT token cannot be refreshed. Please try again.");

            new ObjectMapper().writeValue(res.getOutputStream(), error);
        }
    }
}

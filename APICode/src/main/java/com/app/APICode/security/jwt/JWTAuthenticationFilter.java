package com.app.APICode.security.jwt;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.security.message.AccessTokenMessage;
import com.app.APICode.security.message.CredentialMessage;
import com.app.APICode.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This JWT filter will be activated when Spring Boot initiates the
 * UsernamePasswordAuthenticationFilter This will be called when a POST request
 * to the /login endpoint is called
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        CredentialMessage creds;
        try {
            creds = new ObjectMapper().readValue(req.getInputStream(), CredentialMessage.class);
        } catch (Exception e) {
            // if the request parameters are malformed, just declare the credentials as bad
            throw new BadCredentialsException("Authentication Error");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(creds.getUsername(),
                creds.getPassword());
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException {

        User user = (User) auth.getPrincipal();
        
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(APPLICATION_JSON_VALUE);

        AccessTokenMessage message = new AccessTokenMessage(user);
        new ObjectMapper().writeValue(res.getOutputStream(), message);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException failed) throws IOException, ServletException {
                System.out.println(failed);
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(APPLICATION_JSON_VALUE);

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toString());
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        error.put("message", "Incorrect username or password");
        error.put("path", req.getRequestURI());

        new ObjectMapper().writeValue(res.getOutputStream(), error);
    }
}

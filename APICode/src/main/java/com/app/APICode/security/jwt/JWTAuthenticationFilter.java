package com.app.APICode.security.jwt;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    JWTHelper jwtHelper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
                
            User creds = new User();
            try {
                creds = new ObjectMapper().readValue(req.getInputStream(), User.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword());
            return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain, Authentication auth) throws IOException {
        
        User user = (User) auth.getPrincipal();
        String issuer = req.getServerName().toString();

        String accessToken = jwtHelper.generateAccessToken(user, issuer);
        String refreshToken = jwtHelper.generateRefreshToken(user, issuer);
                
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("username", user.getUsername());
        new ObjectMapper().writeValue(res.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException failed) throws IOException, ServletException {

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();
        error.put("error_message", "Incorrect email or password");
        new ObjectMapper().writeValue(res.getOutputStream(), error);
    }
}

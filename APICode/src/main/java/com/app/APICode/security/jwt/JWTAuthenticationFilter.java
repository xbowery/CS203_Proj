package com.app.APICode.security.jwt;

import static com.app.APICode.security.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.app.APICode.security.jwt.SecurityConstants.SECRET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withIssuer(req.getServerName().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
                
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", token);
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

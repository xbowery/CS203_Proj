package com.app.APICode.security;

import static com.app.APICode.security.SecurityConstants.EXPIRATION_TIME;
import static com.app.APICode.security.SecurityConstants.SECRET;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
                // Should be handled by an exception
                System.out.println(e);
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword());
            return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException {
        String token = JWT.create().withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(String.format("{\"username\": \"%s\", \"accessToken\": \"%s\"}",
                ((User) auth.getPrincipal()).getEmail(), token));
        res.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException failed) throws IOException, ServletException {

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(String.format("{ \"message\": \"%s\" }","Incorrect email or password"));
        res.getWriter().flush();
    }
}
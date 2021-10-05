package com.app.APICode.security.jwt;

import static com.app.APICode.security.jwt.SecurityConstants.SECRET;
import static com.app.APICode.security.jwt.SecurityConstants.TOKEN_PREFIX;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (req.getServletPath().equals("/login")) {
            chain.doFilter(req, res);
            return;
        }

        String tokenHeader = req.getHeader(AUTHORIZATION);
        if (tokenHeader == null || !tokenHeader.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenHeader);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);

        } catch (Exception e) {
            res.setContentType(APPLICATION_JSON_VALUE);
            res.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Invalid JWT Token");
            new ObjectMapper().writeValue(res.getOutputStream(), error);
        }
    }

    /**
     * Verifies JWT and return a {@link UsernamePasswordAuthenticationToken}
     * 
     * @param tokenHeader A header value containing the keyword "Bearer" and JWT
     * @return {@link UsernamePasswordAuthenticationToken} Authentication Token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token);
        
        String user = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}

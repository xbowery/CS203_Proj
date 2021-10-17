package com.app.APICode.security.jwt;

import static com.app.APICode.security.jwt.SecurityConstants.TOKEN_PREFIX;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter will be filtering every single request to check if the token
 * exists If the token exists, it will verify if the token is valid and not
 * tampered
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    JWTHelper jwtHelper;

    Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    @Autowired
    public JWTAuthorizationFilter(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // If the token is not avilable or the Authorization header is incorrect, skip
        // this filter
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
            logger.info(e.getMessage());

            res.setContentType(APPLICATION_JSON_VALUE);
            res.setStatus(UNAUTHORIZED.value());

            Map<String, Object> error = new LinkedHashMap<>();
            error.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toString());
            error.put("status", HttpStatus.UNAUTHORIZED.value());
            error.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            error.put("message", "Invalid or missing token");
            error.put("path", req.getRequestURI());

            new ObjectMapper().writeValue(res.getOutputStream(), error);
        }
    }

    /**
     * Check against a list of URI that will bypass this filter
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
        String url = req.getServletPath().toString();
        return isResourceUrl(url);
    }

    private boolean isResourceUrl(String url) {
        List<String> resourceRequests = Arrays.asList("/login", "/register", "/refreshToken");

        // If it is a match, return true
        for (String resourceRequest : resourceRequests) {
            if (url.equals(resourceRequest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies JWT and return a {@link UsernamePasswordAuthenticationToken} This
     * function converts a JWT into this token which is recognised by Spring Boot to
     * perform further filters or to perform more Authorization checks
     * 
     * @param tokenHeader A header value containing the keyword "Bearer" and JWT
     * @return {@link UsernamePasswordAuthenticationToken} Authentication Token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = jwtHelper.decodeJwt(token);

        // Check if it is an accessToken, since only access tokens have their roles
        if (!decodedJWT.getClaim("type").asString().equals("access")) {
            throw new BadCredentialsException("Incorrect token type");
        }

        String user = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
}

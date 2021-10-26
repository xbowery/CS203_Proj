package com.app.APICode.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.APICode.security.jwt.JWTRefreshToken;
import com.app.APICode.security.model.Credential;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    JWTRefreshToken refreshToken;

    @Autowired
    public AuthenticationController(JWTRefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    @PostMapping("/login")
    public void login(Credential credential) {
        throw new NotImplementedException("/login should not be called");
    }

    /**
     * Refreshes the JWT token
     * 
     * @param req a HttpServletRequest object
     * @param res a HttpServletResponse cookie
     * @throws IOException
     */
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        refreshToken.refreshJwtToken(req, res);
    }

    
}

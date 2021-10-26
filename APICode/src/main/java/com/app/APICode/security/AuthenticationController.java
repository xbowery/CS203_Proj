package com.app.APICode.security;

import java.io.IOException;

import com.app.APICode.security.jwt.JWTRefreshToken;
import com.app.APICode.security.message.AccessTokenMessage;
import com.app.APICode.security.message.CredentialMessage;
import com.app.APICode.security.message.RefreshTokenMessage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    JWTRefreshToken jwtRefreshToken;

    @Autowired
    public AuthenticationController(JWTRefreshToken jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }
    
    @PostMapping("/login")
    public AccessTokenMessage login(CredentialMessage credential) {
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
    public AccessTokenMessage refreshToken(@RequestBody RefreshTokenMessage refreshToken) {
        return jwtRefreshToken.refreshJwtToken(refreshToken.getRefreshToken());
    }

    
}

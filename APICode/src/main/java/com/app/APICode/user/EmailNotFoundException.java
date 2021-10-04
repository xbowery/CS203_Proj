package com.app.APICode.user;

import org.springframework.security.core.AuthenticationException;

public class EmailNotFoundException extends AuthenticationException {
    public EmailNotFoundException (String email) {
        super("This email does not exist: " + email);
    }
}

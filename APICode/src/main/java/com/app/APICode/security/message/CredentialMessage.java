package com.app.APICode.security.message;

import io.swagger.v3.oas.annotations.media.Schema;

public class CredentialMessage {
    @Schema(description = "Username for authentication", example = "JohnDoe", required = true)
    private String username;

    @Schema(description = "Password for authentication", example = "password", required = true)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

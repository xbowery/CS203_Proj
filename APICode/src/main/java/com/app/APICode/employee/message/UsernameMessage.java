package com.app.APICode.employee.message;

import javax.validation.constraints.NotNull;

public class UsernameMessage {
    
    @NotNull
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}

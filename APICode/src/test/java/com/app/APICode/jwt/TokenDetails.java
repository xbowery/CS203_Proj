package com.app.APICode.jwt;

public class TokenDetails {
    private String username;
    private String accessToken;
    private String message;

    public TokenDetails() {
    }

    public TokenDetails(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    public TokenDetails(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

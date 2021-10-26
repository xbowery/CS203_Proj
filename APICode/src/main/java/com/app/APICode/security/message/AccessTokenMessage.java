package com.app.APICode.security.message;

import java.util.stream.Collectors;

import com.app.APICode.security.jwt.JWTHelper;
import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.GrantedAuthority;

public class AccessTokenMessage {
    @JsonProperty("accessToken")
    private String accessTokenString;
    private String refreshToken;
    private String username;
    private String roles;

    public AccessTokenMessage(User user) {
        this.accessTokenString = JWTHelper.generateAccessToken(user);
        this.refreshToken = JWTHelper.generateRefreshToken(user);
        this.username = user.getUsername();
        this.roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0);
    }
    
    public String getAccessTokenString() {
        return accessTokenString;
    }
    public void setAccessTokenString(String accessTokenString) {
        this.accessTokenString = accessTokenString;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
}

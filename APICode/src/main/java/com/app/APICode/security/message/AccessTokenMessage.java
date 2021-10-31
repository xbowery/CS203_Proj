package com.app.APICode.security.message;

import java.util.stream.Collectors;

import com.app.APICode.security.jwt.JWTHelper;
import com.app.APICode.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.v3.oas.annotations.media.Schema;

public class AccessTokenMessage {
    @Schema(description = "JWT Access Token used for authentication", example = "<Header>.<Payload>.<Signature>", required = true)
    @JsonProperty("accessToken")
    private String accessTokenString;

    @Schema(description = "JWT Refresh Token used to get a new Access Token", example = "<Header>.<Payload>.<Signature>", required = true)
    private String refreshToken;

    @Schema(description = "Username of the user", example = "JohnDoe", required = true)
    private String username;

    @Schema(description = "Roles that the user have", example = "ROLE_USER", required = true)
    private String role;

    public AccessTokenMessage(User user) {
        this.accessTokenString = JWTHelper.generateAccessToken(user);
        this.refreshToken = JWTHelper.generateRefreshToken(user);
        this.username = user.getUsername();
        this.role = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                .get(0);
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

    public String getRole() {
        return role;
    }

    public void setRoles(String role) {
        this.role = role;
    }
}

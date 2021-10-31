package com.app.APICode.security.message;

import io.swagger.v3.oas.annotations.media.Schema;

public class RefreshTokenMessage {
    @Schema(description = "JWT Refresh Token of the User", example = "<Header>.<Payload>.<Signature>", required = true)
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

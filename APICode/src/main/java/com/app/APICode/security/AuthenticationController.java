package com.app.APICode.security;

import com.app.APICode.security.jwt.JWTRefreshToken;
import com.app.APICode.security.message.AccessTokenMessage;
import com.app.APICode.security.message.CredentialMessage;
import com.app.APICode.security.message.RefreshTokenMessage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    private JWTRefreshToken jwtRefreshToken;

    @Autowired
    public AuthenticationController(JWTRefreshToken jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }

    @Operation(summary = "Login", description = "Login to get JWT Access Token", tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessTokenMessage.class))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content) })
    @PostMapping("/login")
    public AccessTokenMessage login(CredentialMessage credential) {
        throw new NotImplementedException("/login should not be called");
    }

    /**
     * Refreshes the JWT token
     * 
     * @param RefreshTokenMessage containing the refreshToken
     */
    @Operation(summary = "Refreshes JWT token", description = "Get a new JWT Access Token with Refresh Token", tags = {
            "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful refresh of token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessTokenMessage.class))),
            @ApiResponse(responseCode = "401", description = "Invalid JWT or JWT has expired", content = @Content) })
    @PostMapping("/refreshToken")
    public AccessTokenMessage refreshToken(@RequestBody RefreshTokenMessage refreshToken) {
        return jwtRefreshToken.refreshJwtToken(refreshToken.getRefreshToken());
    }

}

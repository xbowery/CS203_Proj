package com.app.APICode.verificationtoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Verification Token", description = "Verification Token for Registration API")
public class VerificationTokenController {

    private VerificationTokenService verificationTokenService;

    @Autowired
    public VerificationTokenController (VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }
}

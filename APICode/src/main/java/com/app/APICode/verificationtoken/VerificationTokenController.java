package com.app.APICode.verificationtoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Verification Token", description = "Verification Token for Registration API")
public class VerificationTokenController {

    private VerificationTokenService verificationTokenService;

    @Autowired
    public VerificationTokenController(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    /**
     * Function to call to confirm a user's registration
     * 
     * @param token
     * @return
     */
    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") final String token) {
        return verificationTokenService.validateVerificationToken(token);
    }
}

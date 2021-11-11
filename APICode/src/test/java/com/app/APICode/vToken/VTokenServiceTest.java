package com.app.APICode.vToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.user.User;
import com.app.APICode.user.UserService;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;
import com.app.APICode.verificationtoken.VerificationTokenServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VTokenServiceTest {

    @Mock
    private EmailerService emailerService;

    @Mock
    private UserService userService;

    @Mock
    private VerificationTokenRepository vTokens;

    @InjectMocks
    private VerificationTokenServiceImpl vTokenService;

    @Test
    void getToken_ValidVerificationToken_ReturnToken() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        VerificationToken vToken = new VerificationToken("validToken", user);
        when(vTokens.findByToken(vToken.getToken())).thenReturn(Optional.of(vToken));

        // Act
        VerificationToken vTokenObtained = vTokenService.getVerificationToken(vToken.getToken());

        // Assert
        assertNotNull(vTokenObtained);
        verify(vTokens).findByToken(vToken.getToken());
    }

    @Test
    void validateVerificationToken_ValidToken_ReturnValid() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        VerificationToken vToken = new VerificationToken("validToken", user);
        when(vTokens.findByToken(vToken.getToken())).thenReturn(Optional.of(vToken));

        // Act
        String result = vTokenService.validateVerificationToken(vToken.getToken());

        // Assert
        assertNotNull(result);
        assertEquals("valid", result);
        verify(vTokens).findByToken(vToken.getToken());
    }

    @Test
    void validateVerificationToken_InvalidToken_ReturnInvalid() {
        // Arrange
        String token = "nosuchtoken";
        when(vTokens.findByToken(token)).thenReturn(Optional.empty());

        // Act
        String result = vTokenService.validateVerificationToken(token);

        // Assert
        assertNotNull(result);
        assertEquals("invalidToken", result);
    }
}

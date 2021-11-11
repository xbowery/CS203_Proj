package com.app.APICode.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.emailer.EmailerServiceImpl;
import com.app.APICode.user.EmailNotFoundException;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserOrEmailExistsException;
import com.app.APICode.user.UserRepository;
import com.app.APICode.user.UserServiceImpl;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository users;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private EmailerServiceImpl emailerService;

    @Mock
    private VerificationTokenRepository vTokens;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_NewUsername_ReturnSavedUser() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");

        when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(users.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO savedUser = userService.addUser(user, true);

        // Assert
        assertNotNull(savedUser);
        verify(users).findByUsername(user.getUsername());
        verify(users).save(user);
    }

    @Test
    void addUser_ExistingUser_ReturnNull() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");

        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        // Act
        UserOrEmailExistsException existsException = assertThrows(UserOrEmailExistsException.class, () -> {
            userService.addUser(user, true);
        });

        // Assert
        assertEquals(existsException.getMessage(), "This email already exists. Please sign in instead.");
        verify(users).findByUsername(user.getUsername());
        verify(users, never()).save(user);
    }

    @Test
    void addUser_ExistingUserEmail_ReturnNull() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        User user2 = user;
        user2.setUsername("user2");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        // Act
        UserOrEmailExistsException existsException = assertThrows(UserOrEmailExistsException.class, () -> {
            userService.addUser(user2, true);
        });

        // Assert
        assertEquals(existsException.getMessage(), "This email already exists. Please sign in instead.");
        verify(users).findByUsername(user.getUsername());
        verify(users, never()).save(user2);
    }

    @Test
    void updateUser_NewEmail_ReturnUpdatedUser() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        User user2 = user;
        user2.setEmail("usertest@test.com");
        ReflectionTestUtils.setField(user2, "id", 1L);

        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act

        userService.updateUserByUsername("user1", UserDTO.convertToUserDTO(user2));

        // Assert
        verify(users).findByUsername(user.getUsername());
        verify(users).setUserInfoByUsername(user2.getFirstName(), user2.getLastName(), user2.getEmail(),
                user2.getUsername());
    }

    @Test
    void updateUser_ExistingEmail_ReturnException() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        User updatedUser = new User("user2@test.com", "user1", "User", "one", "", false, "ROLE_USER");

        User existingUser = new User("user2@test.com", "existingUser", "User", "one", "", false, "ROLE_USER");

        when(users.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(users.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // Act
        UserOrEmailExistsException existsException = assertThrows(UserOrEmailExistsException.class, () -> {
            userService.updateUserByUsername(user.getUsername(), UserDTO.convertToUserDTO(updatedUser));
        });

        // Assert
        assertEquals(existsException.getMessage(), "This email already exists.");
        verify(users).findByUsername(user.getUsername());
        verify(users).findByEmail(existingUser.getEmail());
    }

    @Test
    void deleteUser_ValidUser_ReturnNull() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        String username = user.getUsername();
        when(users.existsByUsername(username)).thenReturn(true);
        when(users.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(username);

        // Assert
        verify(users).deleteByUsername(username);
    }

    @Test
    void deleteUser_InvalidUser_ReturnError() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        String username = user.getUsername();

        // Act
        UserNotFoundException notFoundException = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(username);
        });

        // Assert
        assertEquals(notFoundException.getMessage(), "Could not find user with username: " + username);
    }

    @Test
    void getUserByUsername_ValidUser_ReturnUser() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        User userReceived = userService.getUserByUsername(user.getUsername());

        // Assert
        assertEquals(user.getUsername(), userReceived.getUsername());
        verify(users).findByUsername(user.getUsername());
    }

    @Test
    void getUserByUsername_InvalidUser_ReturnNull() {
        // Arrange
        String username = "username";
        when(users.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        UserNotFoundException notFoundException = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByUsername(username);
        });

        // Assert
        assertEquals(notFoundException.getMessage(), "Could not find user with username: " + username);
        verify(users).findByUsername(username);
    }

    @Test
    void getUserByEmail_ValidUser_ReturnUser() {
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        User userReceived = userService.getUserByEmail(user.getEmail());

        // Assert
        assertEquals(user.getEmail(), userReceived.getEmail());
        verify(users).findByEmail(user.getEmail());
    }

    @Test
    void getUserByEmail_InvalidUser_ReturnNull() {
        String email = "nosuchuser@test.com";
        when(users.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        EmailNotFoundException notFoundException = assertThrows(EmailNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });

        // Assert
        assertEquals(notFoundException.getMessage(), "This email does not exist: " + email);
        verify(users).findByEmail(email);
    }

    @Test
    void getToken_ValidVerificationToken_ReturnToken() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        VerificationToken vToken = new VerificationToken("validToken", user);
        when(vTokens.findByToken(vToken.getToken())).thenReturn(Optional.of(vToken));

        // Act
        VerificationToken vTokenObtained = userService.getVerificationToken(vToken.getToken());

        // Assert
        assertNotNull(vTokenObtained);
        verify(vTokens).findByToken(vToken.getToken());
    }

    @Test
    void getToken_InvalidVerificationToken_ReturnNull() {
        // Arrange
        String token = "nosuchtoken";
        when(vTokens.findByToken(token)).thenReturn(Optional.empty());

        // Act
        VerificationToken vToken = vTokens.findByToken(token).orElse(null);

        // Assert
        assertNull(vToken);
        verify(vTokens, never()).save(vToken);
    }

    @Test
    void validateVerificationToken_ValidToken_ReturnValid() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "One", "", false, "ROLE_USER");
        VerificationToken vToken = new VerificationToken("validToken", user);
        when(vTokens.findByToken(vToken.getToken())).thenReturn(Optional.of(vToken));

        // Act
        String result = userService.validateVerificationToken(vToken.getToken());

        // Assert
        assertNotNull(result);
        assertEquals("valid", result);
        verify(vTokens).findByToken(vToken.getToken());
    }

    // @Test
    // void validateVerificationToken_ExpiredToken_ReturnExpired() {
    // // Arrange
    // User user = new User("user@test.com", "user1", "User", "One", "", false,
    // "ROLE_USER");
    // VerificationToken vToken = new VerificationToken("expiredToken", user);
    // vToken.setExpiryDate(new Date(System.currentTimeMillis()));
    // when(vTokens.save(any(VerificationToken.class))).thenReturn(vToken);

    // // Act
    // String result = userService.validateVerificationToken(vToken.getToken());

    // // Assert
    // assertNotNull(result);
    // assertEquals("expired", result);
    // verify(vTokens).save(vToken);
    // }

    @Test
    void validateVerificationToken_InvalidToken_ReturnInvalid() {
        // Arrange
        String token = "nosuchtoken";
        when(vTokens.findByToken(token)).thenReturn(Optional.empty());

        // Act
        String result = userService.validateVerificationToken(token);

        // Assert
        assertNotNull(result);
        assertEquals("invalidToken", result);
    }
}

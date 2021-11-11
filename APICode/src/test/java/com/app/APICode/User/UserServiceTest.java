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

import java.util.Optional;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.emailer.EmailerServiceImpl;
import com.app.APICode.user.User;
import com.app.APICode.user.UserDTO;
import com.app.APICode.user.UserNotFoundException;
import com.app.APICode.user.UserOrEmailExistsException;
import com.app.APICode.user.UserRepository;
import com.app.APICode.user.UserServiceImpl;
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
        verify(users).setUserInfoByUsername(user2.getFirstName(), user2.getLastName(), user2.getEmail(), user2.getUsername());
    }

    // @Test
    // void updateUser_ExistingEmail_ReturnException() {
    //     // Arrange
    //     User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
    //     User user2 = new User("user2@test.com", "user2", "User", "one", "", false, "ROLE_USER");
    //     ReflectionTestUtils.setField(user2, "id", 1L);

    //     when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    //     when(users.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));

    //     // Act
    //     user2.setEmail("user@test.com");
    //     UserOrEmailExistsException existsException = assertThrows(UserOrEmailExistsException.class, () -> {
    //         userService.updateUserByUsername(user2.getUsername(), UserDTO.convertToUserDTO(user2));
    //     });

    //     assertEquals(existsException.getMessage(), "This email already exists.");
    //     verify(users).findByUsername(user.getUsername());
    //     verify(users).findByUsername(user2.getUsername());
    // }

    // @Test
    // void getUser_ValidUser_ReturnUser() {

    // }

    // @Test
    // void getUser_InvalidUser_ReturnNull() {

    // }

    @Test
    void deleteUser_ValidUser_ReturnNull() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        String username = user.getUsername();
        when(users.existsByUsername(username)).thenReturn(true);
        when(users.findByUsername(username)).thenReturn(Optional.of(user));

        //Act
        userService.deleteUser(username);
        
        //Assert 
        verify(users).deleteByUsername(username);
    }
    
    @Test
    void deleteUser_InvalidUser_ReturnError() {
         // Arrange
         User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
         String username = user.getUsername();

         //Act
         UserNotFoundException notFoundException = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(username);
        });

        //Assert
        assertEquals(notFoundException.getMessage(), "Could not find user with username: " + username);
    }
}

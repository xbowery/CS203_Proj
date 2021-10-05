package com.app.APICode.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.APICode.user.User;
import com.app.APICode.user.UserRepository;
import com.app.APICode.user.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository users;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_NewUsername_ReturnSavedUser() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");

        when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(users.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.addUser(user);

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
        User savedUser = userService.addUser(user);

        // Assert
        assertNull(savedUser);
        verify(users).findByUsername(user.getUsername());
        verify(users, never()).save(user);
    }

    @Test
    void updateUser_NewEmail_ReturnUpdatedUser() {
        // Arrange
        User user = new User("user@test.com", "user1", "User", "one", "", false, "ROLE_USER");
        User user2 = new User("updated@test.com", "user1", "User", "one", "", false, "ROLE_USER");

        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(encoder.encode(any(String.class))).thenReturn(user.getPassword());
        when(users.save(any(User.class))).thenReturn(user2);

        // Act
        User updatedUser = userService.updateUserByUsername(user.getUsername(), user2);

        // Assert
        assertNotNull(updatedUser);
        verify(users, atMost(2)).findByUsername(user.getUsername());
        verify(users).save(user);
    }
}

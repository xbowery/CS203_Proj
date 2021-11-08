package com.app.APICode.user;

import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.passwordresettoken.PasswordResetToken;

import java.util.List;

public interface UserService {
    List<UserDTO> listUsers();

    User getUserByUsername(String username);

    User getUserVerifiedByUsername(String requesterName, String username);

    User getUserByEmail(String email);

    VerificationToken getVerificationToken(String VerificationToken);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewVerificationToken(String existingVerificationToken);

    String validateVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    UserDTO addUser(User user, Boolean isAdmin);

    UserDTO updateUserByUsername(String username, UserDTO user);

    User updatePasswordByEmail(String email, String password);

    void createTempPassword(String email) throws EmailNotFoundException;

    void deleteUser(String username);

    Long getUserIdByUsername(String username);

    User getUserById(Long id);

    User save(User user);
}

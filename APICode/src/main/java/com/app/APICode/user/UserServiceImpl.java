package com.app.APICode.user;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.utility.RandomPassword;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;
import com.app.APICode.passwordresettoken.PasswordResetToken;
import com.app.APICode.passwordresettoken.PasswordResetTokenRepository;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository users;

    private VerificationTokenRepository vTokens;

    private PasswordResetTokenRepository pTokens;

    EmailerService emailerService;

    RandomPassword randomPasswordGenerator;

    BCryptPasswordEncoder encoder;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    public UserServiceImpl(UserRepository users, VerificationTokenRepository vTokens, PasswordResetTokenRepository pTokens, EmailerService emailerService, RandomPassword randomPasswordGenerator,
            BCryptPasswordEncoder encoder) {
        this.users = users;
        this.vTokens = vTokens;
        this.pTokens = pTokens;
        this.emailerService = emailerService;
        this.randomPasswordGenerator = randomPasswordGenerator;
        this.encoder = encoder;
    }

    @Override
    public void save(User user) {
        users.save(user);
    }

    @Override
    public List<UserDTO> listUsers() {
        List<User> usersList = users.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user: usersList) {
            userDTOList.add(UserDTO.convertToUserDTO(user));
        }
        return userDTOList;
    }

    @Override
    public User getUserByUsername(String username) {
        return users.findByUsername(username).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return users.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByVerificationToken(String verificationToken) {
        final VerificationToken token = vTokens.findByToken(verificationToken).orElse(null);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return vTokens.findByToken(VerificationToken).orElse(null);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        vTokens.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = vTokens.findByToken(existingVerificationToken).orElse(null);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = vTokens.save(vToken);
        return vToken;
    }

    /**
     * Add logic to validate verification token by calculating the expiry date of token
     */
    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken vToken = vTokens.findByToken(token).orElse(null);
        if (vToken == null) {
            return TOKEN_INVALID;
        }

        final User user = vToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            vTokens.delete(vToken);
            return TOKEN_EXPIRED;
        }
        user.setEnabled(true);
        users.save(user);
        return TOKEN_VALID;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        pTokens.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return pTokens.findByToken(token);
    }

    @Override
    public User getUserByPasswordResetToken(final String token) {
        return pTokens.findByToken(token).getUser();
    }
    
    /**
     * Add logic to avoid adding users with the same email or username
     * Return null if there exists a user with the same email or username
     * 
     * @param user a User object
     * @param isAdmin boolean value to determine if user is admin or not
     * @return the newly added user object
     */
    @Override
    public User addUser(User user, Boolean isAdmin) {
        List<User> sameUsernames = users.findByUsername(user.getUsername()).map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        if (getUserByEmail(user.getEmail()) != null) {
            throw new UserOrEmailExistsException("This email already exists. Please sign in instead.");
        }

        // If the user is not admin, force the default authorities to be ROLE_USER and
        // vaccination to false instead of allowing them to arbitrary set it
        if (!isAdmin) {
            user.setAuthorities(UserRole.USER.role);
            user.setIsVaccinated(false);
        }

        if (sameUsernames.size() == 0) {
            String token = UUID.randomUUID().toString();
            
            Map<String, Object> dataModel = emailerService.getDataModel();
            dataModel.put("isRegisterConfirmation", true);
            dataModel.put("token", token);

            // try {
            //     emailerService.sendMessage(user.getEmail(), dataModel);
            // } catch (MessagingException e) {
            //     System.out.println("Error occurred while trying to send an email to: " + user.getEmail());
            // } catch (IOException e) {
            //     System.out.println("Error occurred while trying to send an email to: " + user.getEmail());
            // }

            User savedUser = users.save(user);
            VerificationToken vToken = new VerificationToken(token, user);
            vTokens.save(vToken);

            return savedUser;
        } else {
            throw new UserOrEmailExistsException("This username is already used. Please choose another username");
        }
    }

    @Override
    public User updateUserByUsername(String username, User newUserInfo) {
        return users.findByUsername(username).map(user -> {
            // Check if email exists to prevent a unique index violation
            if (getUserByEmail(newUserInfo.getEmail()) == null) {
                
            } else if (!(getUserByEmail(newUserInfo.getEmail()).getUsername().equals(username))) {
                return null;
            }

            user.setEmail(newUserInfo.getEmail());
            user.setFirstName(newUserInfo.getFirstName());
            user.setLastName(newUserInfo.getLastName());
            user.setPassword(encoder.encode(newUserInfo.getPassword()));
            // user.setUsername(newUserInfo.getUsername());
            user.setIsVaccinated(newUserInfo.getIsVaccinated());
            return users.save(user);
        }).orElse(null);
    }

    @Override
    public User updatePasswordByEmail(String email, String password) {
        return users.findByEmail(email).map(user -> {
            user.setPassword(encoder.encode(password));
            return users.save(user);
        }).orElse(null);
    }

    @Override
    public void createTempPassword(String email) throws EmailNotFoundException {
        if (users.findByEmail(email) == null) {
            throw new EmailNotFoundException(email);
        }

        String tempPassword = randomPasswordGenerator.generatePassayPassword();
        Map<String, Object> dataModel = emailerService.getDataModel();
        dataModel.put("requestPasswordChange", true);
        dataModel.put("password", tempPassword);

        try {
            emailerService.sendMessage(email, dataModel);
        } catch (MessagingException e) {
            System.out.println("Error occurred while trying to send an email to: " + email);
        } catch (IOException e) {
            System.out.println("Error occurred while trying to send an email to: " + email);
        }
        updatePasswordByEmail(email, encoder.encode(tempPassword));
    }

    @Override
    public void deleteUser(String username) {
        User user = users.findByUsername(username).orElse(null);

        final VerificationToken verificationToken = vTokens.findByUser(user).orElse(null);

        if (verificationToken != null) {
            vTokens.delete(verificationToken);
        }

        users.deleteByUsername(username);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return users.findIdByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return users.getById(id);
    }
}

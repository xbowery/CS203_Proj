package com.app.APICode.user;

import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.notification.NotificationService;
import com.app.APICode.passwordresettoken.PasswordResetToken;
import com.app.APICode.passwordresettoken.PasswordResetTokenRepository;
import com.app.APICode.utility.RandomPassword;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository users;

    private VerificationTokenRepository vTokens;

    private PasswordResetTokenRepository pTokens;

    private NotificationService notificationService;

    EmailerService emailerService;

    RandomPassword randomPasswordGenerator;

    BCryptPasswordEncoder encoder;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    public UserServiceImpl(UserRepository users, VerificationTokenRepository vTokens,
            PasswordResetTokenRepository pTokens, EmailerService emailerService, RandomPassword randomPasswordGenerator,
            BCryptPasswordEncoder encoder, NotificationService notificationService) {
        this.users = users;
        this.vTokens = vTokens;
        this.pTokens = pTokens;
        this.emailerService = emailerService;
        this.randomPasswordGenerator = randomPasswordGenerator;
        this.encoder = encoder;
        this.notificationService = notificationService;
    }

    @Override
    public User save(User user) {
        return users.save(user);
    }

    @Override
    public List<UserDTO> listUsers() {
        List<User> usersList = users.findAll();
        return usersList.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.convertToUserDTO(user);
    }

    @Override
    public List<User> getAllUsers() {
        return users.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        User user = users.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = users.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(email);
        }
        return user;
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
     * Add logic to validate verification token by calculating the expiry date of
     * token.
     */
    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken vToken = vTokens.findByToken(token).orElse(null);
        if (vToken == null) {
            return TOKEN_INVALID;
        }

        final User user = vToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
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
     * Add logic to avoid adding users with the same email or username Return null
     * if there exists a user with the same email or username. Then create a
     * notification.
     * 
     * @param user    a User object
     * @param isAdmin boolean value to determine if user is admin or not
     * @return the newly added user object
     */
    @Override
    public UserDTO addUser(User user, Boolean isAdmin) {
        User duplicateUsername = users.findByUsername(user.getUsername()).orElse(null);
        User duplicateEmail = users.findByEmail(user.getEmail()).orElse(null);

        if (duplicateUsername != null || duplicateEmail != null) {
            throw new UserOrEmailExistsException("This email already exists. Please sign in instead.");
        }

        // If the user is not admin, force the default authorities to be ROLE_USER and
        // vaccination to false instead of allowing them to arbitrary set it
        if (!isAdmin) {
            user.setAuthorities(UserRole.USER.role);
            user.setIsVaccinated(false);
        }

        user.setPassword(encoder.encode(user.getPassword()));

        String token = UUID.randomUUID().toString();

        Map<String, Object> dataModel = emailerService.getDataModel();
        dataModel.put("isRegisterConfirmation", true);
        dataModel.put("token", "http://localhost:3000/RegisterConfirmation?token=" + token);

        // try {
        // emailerService.sendMessage(user.getEmail(), dataModel);
        // } catch (MessagingException e) {
        // System.out.println("Error occurred while trying to send an email to: " +
        // user.getEmail());
        // } catch (IOException e) {
        // System.out.println("Error occurred while trying to send an email to: " +
        // user.getEmail());
        // }

        User savedUser = users.save(user);
        VerificationToken vToken = new VerificationToken(token, user);
        vTokens.save(vToken);

        String notificationText = String.format("Welcome to Swisshack, %s!", savedUser.getUsername());
        notificationService.addNewNotification(notificationText, savedUser);

        return convertToUserDTO(savedUser);
    }

    @Override
    public UserDTO updateUserByUsername(String username, UserDTO newUserInfo) {
        User user = getUserByUsername(username);

        if (!((StringUtils.collectionToCommaDelimitedString(user.getAuthorities()).split("_")[1]).equals("ADMIN"))
                && !(username.equals(newUserInfo.getUsername()))) {
            throw new UserForbiddenException("You are forbidden from processing this request.");
        }

        // Check if email exists to prevent a unique index violation
        User duplicateEmail = users.findByEmail(newUserInfo.getEmail()).orElse(null);
        if (duplicateEmail != null) {
            if (!(duplicateEmail).getUsername().equals(newUserInfo.getUsername())) {
                throw new UserOrEmailExistsException("This email already exists.");
            }
        }
        // user.setEmail(newUserInfo.getEmail());
        // user.setFirstName(newUserInfo.getFirstName());
        // user.setLastName(newUserInfo.getLastName());

        String email = newUserInfo.getEmail();
        String firstName = newUserInfo.getFirstName();
        String lastName = newUserInfo.getLastName();

        // user.setPassword(encoder.encode(newUserInfo.getPassword()));
        // user.setUsername(newUserInfo.getUsername());
        // user.setIsVaccinated(newUserInfo.getIsVaccinated());
        // user.setAuthorities(newUserInfo.getAuthorities());
        users.setUserInfoByUsername(firstName, lastName, email, newUserInfo.getUsername());

        User savedUser = getUserByUsername(newUserInfo.getUsername());
        return convertToUserDTO(savedUser);

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

        // try {
        // emailerService.sendMessage(email, dataModel);
        // } catch (MessagingException e) {
        // System.out.println("Error occurred while trying to send an email to: " +
        // email);
        // } catch (IOException e) {
        // System.out.println("Error occurred while trying to send an email to: " +
        // email);
        // }
        updatePasswordByEmail(email, tempPassword);
    }

    @Override
    public void deleteUser(String username) {
        User user = getUserByUsername(username);

        try {
            final VerificationToken verificationToken = vTokens.findByUser(user).orElse(null);
            if (verificationToken != null) {
                vTokens.delete(verificationToken);
            }

            users.deleteByUsername(username);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(username);
        }

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

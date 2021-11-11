package com.app.APICode.user;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.app.APICode.emailer.EmailerServiceImpl;
import com.app.APICode.user.message.ChangePasswordMessage;
import com.app.APICode.utility.RandomPassword;
import com.app.APICode.verificationtoken.VerificationToken;
import com.app.APICode.verificationtoken.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository users;

    EmailerServiceImpl emailerService;

    RandomPassword randomPasswordGenerator;

    BCryptPasswordEncoder encoder;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    public UserServiceImpl(UserRepository users, EmailerServiceImpl emailerService,
            RandomPassword randomPasswordGenerator, BCryptPasswordEncoder encoder) {
        this.users = users;
        this.emailerService = emailerService;
        this.randomPasswordGenerator = randomPasswordGenerator;
        this.encoder = encoder;
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
    public UserDTO getUserDetailsByUsername(String requesterUsername, String username) {
        User requester = users.findByUsername(requesterUsername).orElse(null);

        if (!(requesterUsername.equals(username))
                && !((StringUtils.collectionToCommaDelimitedString(requester.getAuthorities()).split("_")[1])
                        .equals("ADMIN"))) {
            throw new UserForbiddenException("You are forbidden from processing this request.");
        }

        User user = users.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return convertToUserDTO(user);
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
            throw new EmailNotFoundException(email);
        }
        return user;
    }

    /**
     * Add logic to avoid adding users with the same email or username Return null
     * if there exists a user with the same email or username
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

        final VerificationToken vToken = new VerificationToken(token, user);
        user.setvToken(vToken);
        User savedUser = users.save(user);

        return convertToUserDTO(savedUser);
    }

    @Override
    public void updateUserByUsername(String username, UserDTO newUserInfo) {
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
    }

    @Override
    public User updatePasswordByEmail(String email, String password) {
        return users.findByEmail(email).map(user -> {
            user.setPassword(encoder.encode(password));
            return users.save(user);
        }).orElse(null);
    }

    @Override
    public void createTempPassword(String email) {
        try {
            getUserByEmail(email);
        } catch (EmailNotFoundException e) {
            throw new NoContentResponse();
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
    @Transactional
    public void deleteUser(String username) {
        if (users.existsByUsername(username)) {
            users.deleteByUsername(username);
            return;
        }
        throw new UserNotFoundException(username);
    }

    @Override
    public void changePasswordByUsername(String username, ChangePasswordMessage message) {
        if (!message.getNewPassword().equals(message.getCfmPassword())) {
            throw new InvalidChangePasswordException("New Password and Confirm Password does not match.");
        }

        User user = getUserByUsername(username);
        if (!encoder.matches(message.getCurrentPassword(), user.getPassword())) {
            throw new InvalidChangePasswordException("Current Password does not match.");
        }

        if (message.getCurrentPassword().equals(message.getCfmPassword())) {
            throw new InvalidChangePasswordException("Please change to a new password instead.");
        }

        String encodedPassword = encoder.encode(message.getNewPassword());
        user.setPassword(encodedPassword);
        users.save(user);
    }
}

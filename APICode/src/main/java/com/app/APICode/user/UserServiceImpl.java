package com.app.APICode.user;

import com.app.APICode.emailer.EmailerService;
import com.app.APICode.utility.*;

import java.io.IOException;
import java.util.*;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository users;

    EmailerService emailerService;

    RandomPassword randomPasswordGenerator;

    BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository users, EmailerService emailerService, RandomPassword randomPasswordGenerator,
            BCryptPasswordEncoder encoder) {
        this.users = users;
        this.emailerService = emailerService;
        this.randomPasswordGenerator = randomPasswordGenerator;
        this.encoder = encoder;
    }

    @Override
    public List<User> listUsers() {
        return users.findAll();
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
    public User addUser(User user, Boolean isAdmin) {
        List<User> sameUsernames = users.findByUsername(user.getUsername()).map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        // If the user is not admin, force the default authorities to be ROLE_USER and
        // vaccination to false instead of allowing them to arbitrary set it
        if (!isAdmin) {
            user.setAuthorities(UserRole.USER.role);
            user.setIsVaccinated(false);
        }

        if (sameUsernames.size() == 0) {
            return users.save(user);
        } else {
            return null;
        }
    }

    @Override
    public User updateUserByUsername(String username, User newUserInfo) {
        return users.findByUsername(username).map(user -> {
            user.setEmail(newUserInfo.getEmail());
            user.setFirstName(newUserInfo.getFirstName());
            user.setLastName(newUserInfo.getLastName());
            user.setPassword(encoder.encode(newUserInfo.getPassword()));
            user.setUsername(newUserInfo.getUsername());
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

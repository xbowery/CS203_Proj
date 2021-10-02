package com.app.APICode.user;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository users;

    public UserServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public List<User> listUsers() {
        return users.findAll();
    }

    @Override
    public User getUser(String email) {
        return users.findByEmail(email).orElse(null);
    }

    @Override
    public User addUser(User user) {
        List<User> sameUserEmails = users.findByEmail(user.getEmail())
        .map(Collections::singletonList)
        .orElseGet(Collections::emptyList);

        if (sameUserEmails.size() == 0) {
            return users.save(user);
        } else {
            return null;
        }
    }

    @Override
    public User updateUser(String email, User newUserInfo) {
        return users.findByEmail(email).map(user -> {
            user.setEmail(newUserInfo.getEmail());
            user.setFirstName(newUserInfo.getFirstName());
            user.setLastName(newUserInfo.getLastName());
            user.setPassword(newUserInfo.getPassword());
            user.setUsername(newUserInfo.getPassword());
            user.setVaccinationStatus(newUserInfo.getVaccinationStatus());
            return users.save(user);
        }).orElse(null);
    }

    @Override
    public void deleteUser(String email) {
        users.deleteByEmail(email);
    }
    
}

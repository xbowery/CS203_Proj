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
    public User getUser(String username) {
        return users.findByUsername(username).orElse(null);
    }

    @Override
    public User addUser(User user) {
        List<User> sameUsernames = users.findByUsername(user.getUsername())
        .map(Collections::singletonList)
        .orElseGet(Collections::emptyList);

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
            user.setPassword(newUserInfo.getPassword());
            user.setUsername(newUserInfo.getPassword());
            user.setVaccinationStatus(newUserInfo.getVaccinationStatus());
            return users.save(user);
        }).orElse(null);
    }

    @Override
    public void deleteUser(String username) {
        users.deleteByUsername(username);
    }
    
}

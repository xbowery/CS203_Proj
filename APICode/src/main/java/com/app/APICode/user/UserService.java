package com.app.APICode.user;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(String username);
    User addUser(User user);
    User updateUserByUsername(String username, User user);

    void deleteUser(String username);
}

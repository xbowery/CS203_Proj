package com.app.APICode.user;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(String email);
    User addUser(User user);
    User updateUser(String email, User user);

    void deleteUser(String email);
}

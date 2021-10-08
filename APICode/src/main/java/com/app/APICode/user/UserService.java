package com.app.APICode.user;

import java.util.List;

public interface UserService {
    List<User> listUsers();

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User addUser(User user, Boolean isAdmin);

    User updateUserByUsername(String username, User user);

    User updatePasswordByEmail(String email, String password);

    void createTempPassword(String email) throws EmailNotFoundException;

    void deleteUser(String username);

    Long getUserIdByUsername(String username);

    User getUserById(Long id);
}

package com.app.APICode.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.app.APICode.security.jwt.JWTRefreshToken;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;
    private JWTRefreshToken refreshToken;

    public UserController(UserRepository users, BCryptPasswordEncoder encoder, JWTRefreshToken refreshToken) {
        this.users = users;
        this.encoder = encoder;
        this.refreshToken = refreshToken;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.findAll();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * 
     * @param user
     * @return
     */
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return users.save(user);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException{
        refreshToken.refreshJwtToken(req, res);
    }

}

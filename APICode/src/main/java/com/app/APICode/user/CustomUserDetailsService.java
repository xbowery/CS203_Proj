package com.app.APICode.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository users;
    
    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }
    
    /** To return a UserDetails for Spring Security 
     *  Note that the method takes only a username.
        The UserDetails interface has methods to get the password.
    */
    @Override
    public UserDetails loadUserByUsername(String email)  throws EmailNotFoundException {
        return users.findByEmail(email)
            .orElseThrow(() -> new EmailNotFoundException("No user found with email address:" + email));
    }
    
}


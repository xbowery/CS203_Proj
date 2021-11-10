package com.app.APICode.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // define a derived query to find user by username
    Optional<User> findByUsername(String username);

    Long deleteByUsername(String username);

    Optional<User> findByEmail(String email);

    Long findIdByUsername(String username);

    @Modifying
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.email = ?3 where u.username = ?4")
    User setUserInfoByUsername(String firstname, String lastname, String email, String username);
}

package com.app.APICode.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to allow us to store {@link User} as persistent data through JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // define a derived query to find user by username
    Optional<User> findByUsername(String username);

    Long deleteByUsername(String username);

    Optional<User> findByEmail(String email);

    Long findIdByUsername(String username);

    @Modifying
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.email = ?3 where u.username = ?4")
    void setUserInfoByUsername(String firstname, String lastname, String email, String username);

    @Modifying
    @Query("update User u set u.firstName = ?1, u.lastName = ?2, u.isVaccinated = ?3 where u.username = ?4")
    void setUserInfoByUsername(String firstname, String lastname, boolean isVaccinated, String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

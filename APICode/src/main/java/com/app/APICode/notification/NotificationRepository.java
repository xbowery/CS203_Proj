package com.app.APICode.notification;

import java.util.Optional;

import com.app.APICode.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to allow us to store {@link Notification} as persistent data through JPA.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying()
    @Query("update Notification n set n.isSeen = true where n.user = ?1")
    void updateAllNotificationToRead(User user);

    Optional<Notification> findByIdAndUser(Long id, User user);

}

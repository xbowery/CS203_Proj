package com.app.APICode.notification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query("update Notification n set n.isSeen = true where n.user = ?1")
    void updateAllNotificationToRead(Long user_id);

    Optional<Notification> findByIdAndUser(Long id, Long user_id);

}

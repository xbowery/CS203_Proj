package com.app.APICode.notification;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByDatetime(LocalDateTime datetime);

    Optional<List<Notification>> findByUser(Long user_id);

    @Modifying
    @Query("update Notification n set n.isSeen = true where n.user = ?1")
    void updateAllNotificationToRead(Long user_id);

    Optional<Notification> findByIdAndUser(Long id, Long user_id);

}

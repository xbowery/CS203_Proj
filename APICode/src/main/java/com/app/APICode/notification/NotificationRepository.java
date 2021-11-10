package com.app.APICode.notification;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    Optional<Notification> findByDatetime(LocalDateTime datetime);

    Optional<List<Notification>> findByUser(Long user_id);

}

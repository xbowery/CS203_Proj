package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrowdLevelRepository extends JpaRepository<CrowdLevel, Long> {
    Optional<CrowdLevel> findByRestaurant(Long restaurant_id);
    
    Optional<CrowdLevel> findByDatetime(Date datetime);
}

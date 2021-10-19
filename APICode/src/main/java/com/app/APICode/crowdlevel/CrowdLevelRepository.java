package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.Optional;

import com.app.APICode.restaurant.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrowdLevelRepository extends JpaRepository<CrowdLevel, Long> {
    Optional<CrowdLevel> findByRestaurant(Restaurant restaurant);
    Optional<CrowdLevel> findByDatetime(Date datetime);
}

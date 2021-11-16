package com.app.APICode.crowdlevel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.app.APICode.restaurant.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to allow us to store crowd levels of a restaurant as persistent data through JPA.
 */
@Repository
public interface CrowdLevelRepository extends JpaRepository<CrowdLevel, Long> {
    List<CrowdLevel> findByRestaurant(Restaurant restaurant);
    Optional<CrowdLevel> findByDatetime(Date datetime);
}

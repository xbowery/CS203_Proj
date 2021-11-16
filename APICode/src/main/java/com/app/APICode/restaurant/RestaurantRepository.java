package com.app.APICode.restaurant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to allow us to store {@link Restaurant} as persistent data through JPA.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // define a derived query to find restaurant by name and location
    Optional<Restaurant> findByNameAndLocation(String name, String location);
}


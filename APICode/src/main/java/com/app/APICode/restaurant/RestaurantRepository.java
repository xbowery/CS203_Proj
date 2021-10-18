package com.app.APICode.restaurant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // define a derived query to find restaurant by name and location
    Optional<Restaurant> findByNameAndLocation(String name, String location);
    Long deleteByNameAndLocation(String name, String location);
}


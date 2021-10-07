package com.app.APICode.measure.restaurant;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasureRestaurantRepository extends JpaRepository<MeasureRestaurant, Long> {
    // define a derived query to find MeasureRestaurant by created datetime
    Optional<MeasureRestaurant> findByCreationDateTime(Date CreationDateTime);
}

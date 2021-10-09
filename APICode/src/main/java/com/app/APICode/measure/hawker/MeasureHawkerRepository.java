package com.app.APICode.measure.hawker;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureHawkerRepository extends JpaRepository<MeasureHawker, Long> {
    // define a derived query to find MeasureHawker by created datetime
    Optional<MeasureHawker> findByCreationDateTime(Date creationDateTime);
    Long deleteByCreationDateTime (Date creationDateTime);
}

package com.app.APICode.measure.others;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureOthersRepository extends JpaRepository<MeasureOthers, Long>{
    // define a derived query to find MeasureOthers by created datetime
    Optional<MeasureOthers> findByCreationDateTime(Date creationDateTime);
}

package com.app.APICode.measure;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long>{
        // define a derived query to find Measure by created datetime
        Optional<Measure> findByCreationDateTime(Date creationDateTime);

        //returns number of deleted measures
        Long deleteByCreationDateTime (Date creationDateTime);        

        Measure findTopByOrderByCreationDateTimeDesc();

}

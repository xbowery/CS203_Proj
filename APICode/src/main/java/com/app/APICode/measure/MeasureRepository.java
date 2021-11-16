package com.app.APICode.measure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer to allow us to store {@link Measure} as persistent data through JPA.
 */
@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {

	Optional<Measure> findByMeasureType(String measureType);

	void deleteByMeasureType(String measureType);

	boolean existsByMeasureType(String measureType);
}

package com.app.APICode.ctest;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CtestRepository extends JpaRepository<Ctest, Long>{
    List<Ctest> findByUserId(Long userId);
    Optional<Ctest> findByIdAndUserId(Long id, Long userId);
}

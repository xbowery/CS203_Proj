package com.app.APICode.ctest;



import java.util.List;
import java.util.Optional;

import com.app.APICode.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CtestRepository extends JpaRepository<Ctest, Long>{
    List<Ctest> findByEmployee(Employee employee);
    Optional<Ctest> findByIdAndEmployeeId(Long id, Long employeeId);
    boolean existsByIdAndEmployeeId(Long ctestId, Long employeeId);
}

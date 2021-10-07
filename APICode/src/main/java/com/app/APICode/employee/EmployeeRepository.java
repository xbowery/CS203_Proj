package com.app.APICode.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByRestaurantAndUser(Long restaurant_id, Long user_id);

    Optional<Long> findRestaurantIdByUserId(@Param("user_id") Long user_id);

    Optional<List<Employee>> findByRestaurant(Long restaurant_id);
}
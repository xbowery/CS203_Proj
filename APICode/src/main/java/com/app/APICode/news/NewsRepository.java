package com.app.APICode.news;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findById(Long id);

    void deleteById(Long id);

    News findTopByOrderByArticleDateDesc();
}

package com.syalux.eduhub.repository;

import com.syalux.eduhub.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByName(String name);
    List<University> findByLocationContainingIgnoreCase(String locationKeyword); // Example: Find by country or city
    Page<University> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

package com.syalux.eduhub.repository;

import com.syalux.eduhub.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findByUniversityId(Long universityId);
    List<Major> findByNameContainingIgnoreCase(String majorNameKeyword);
}

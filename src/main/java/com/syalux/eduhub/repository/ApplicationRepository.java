package com.syalux.eduhub.repository;

import com.syalux.eduhub.model.Application;
import com.syalux.eduhub.model.ApplicationStatus;
import com.syalux.eduhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(User student);
    List<Application> findByStudentId(Long studentId);
    List<Application> findByUniversityId(Long universityId);
    List<Application> findByMajorId(Long majorId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByStudentIdAndStatus(Long studentId, ApplicationStatus status);
}

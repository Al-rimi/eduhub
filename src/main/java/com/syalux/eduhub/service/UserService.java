package com.syalux.eduhub.service;

import com.syalux.eduhub.model.User;
import com.syalux.eduhub.model.University;
import java.util.List;

public interface UserService {
    long countUsers();
    List<User> getAllUsers();
    void changeUserRole(Long userId, String role);
    void deleteUser(Long userId);

    // Staff-university assignment methods
    void assignUniversityToStaff(Long staffId, Long universityId);
    void removeUniversityFromStaff(Long staffId, Long universityId);
    List<User> getAllStaff();
    List<University> getAssignedUniversitiesForStaff(Long staffId);
}

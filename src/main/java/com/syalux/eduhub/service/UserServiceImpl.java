package com.syalux.eduhub.service;

import com.syalux.eduhub.model.User;
import com.syalux.eduhub.model.University;
import com.syalux.eduhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.syalux.eduhub.repository.UniversityRepository universityRepository;

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changeUserRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setRole(com.syalux.eduhub.model.Role.valueOf(role));
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void assignUniversityToStaff(Long staffId, Long universityId) {
        User staff = userRepository.findById(staffId).orElse(null);
        University university = null;
        if (staff != null && staff.getRole() == com.syalux.eduhub.model.Role.ROLE_STAFF) {
            university = universityRepository.findById(universityId).orElse(null);
            if (university != null) {
                if (staff.getAssignedUniversities() == null) staff.setAssignedUniversities(new java.util.HashSet<>());
                staff.getAssignedUniversities().add(university);
                userRepository.save(staff);
            }
        }
    }

    @Override
    public void removeUniversityFromStaff(Long staffId, Long universityId) {
        User staff = userRepository.findById(staffId).orElse(null);
        if (staff != null && staff.getRole() == com.syalux.eduhub.model.Role.ROLE_STAFF && staff.getAssignedUniversities() != null) {
            staff.getAssignedUniversities().removeIf(u -> u.getId().equals(universityId));
            userRepository.save(staff);
        }
    }

    @Override
    public List<User> getAllStaff() {
        return userRepository.findAll().stream()
            .filter(u -> u.getRole() == com.syalux.eduhub.model.Role.ROLE_STAFF)
            .collect(Collectors.toList());
    }

    @Override
    public List<University> getAssignedUniversitiesForStaff(Long staffId) {
        User staff = userRepository.findById(staffId).orElse(null);
        if (staff != null && staff.getRole() == com.syalux.eduhub.model.Role.ROLE_STAFF && staff.getAssignedUniversities() != null) {
            return new java.util.ArrayList<>(staff.getAssignedUniversities());
        }
        return java.util.Collections.emptyList();
    }
}

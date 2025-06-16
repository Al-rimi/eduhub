package com.syalux.eduhub.service;

import com.syalux.eduhub.dto.MajorDTO;
import com.syalux.eduhub.dto.UniversityCreateUpdateDTO;
import com.syalux.eduhub.dto.UniversityDTO;
import com.syalux.eduhub.model.Major;
import com.syalux.eduhub.model.University;
import com.syalux.eduhub.model.User;
import com.syalux.eduhub.repository.UniversityRepository;
import com.syalux.eduhub.repository.UserRepository; // Assuming facility admin is a user
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository; // For linking facility to user if needed

    @Autowired
    private AuthService authService; // To get current user for authorization

    // --- DTO Converters ---
    private UniversityDTO convertToDTO(University university) {
        if (university == null) return null;
        UniversityDTO dto = new UniversityDTO();
        dto.setId(university.getId());
        dto.setName(university.getName());
        dto.setDescription(university.getDescription());
        dto.setLocation(university.getLocation());
        dto.setImageUrl(university.getImageUrl());
        dto.setRequirements(university.getRequirements());
        if (university.getMajors() != null) {
            java.util.List<Major> majorsCopy = new java.util.ArrayList<>(university.getMajors());
            dto.setMajors(majorsCopy.stream().map(this::convertMajorToDTO).collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }

    private MajorDTO convertMajorToDTO(Major major) {
        if (major == null) return null;
        MajorDTO dto = new MajorDTO();
        dto.setId(major.getId());
        dto.setName(major.getName());
        dto.setDescription(major.getDescription());
        if (major.getUniversity() != null) {
            dto.setUniversityId(major.getUniversity().getId());
            dto.setUniversityName(major.getUniversity().getName());
        }
        return dto;
    }

    // --- Public Service Methods ---

    @Transactional(readOnly = true)
    public List<UniversityDTO> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UniversityDTO> getUniversityById(Long id) {
        return universityRepository.findById(id).map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<University> getUniversityEntityById(Long id) {
        return universityRepository.findById(id);
    }

    // Only platform admin or facility admin (for their own university) can create/update
    // For simplicity, this example allows platform admin to create any.
    // Facility admin creation might involve linking to their user account.
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_FACILITY_ADMIN')")
    @Transactional
    public UniversityDTO createUniversity(UniversityCreateUpdateDTO universityCreateDTO) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated.");
        }

        University university = new University();
        university.setName(universityCreateDTO.getName());
        university.setDescription(universityCreateDTO.getDescription());
        university.setLocation(universityCreateDTO.getLocation());
        university.setImageUrl(universityCreateDTO.getImageUrl());
        university.setRequirements(universityCreateDTO.getRequirements());

        // If the user is a FACILITY_ADMIN, we might want to associate this university with them.
        // This requires adding a field to University entity like `managedBy` (User)
        // or a join table for multiple facility admins per university.
        // For now, this is a simplified version.
        // if (currentUser.getRole() == Role.ROLE_FACILITY_ADMIN) {
        //    // Check if this facility admin already manages a university or link it.
        // }

        University savedUniversity = universityRepository.save(university);
        return convertToDTO(savedUniversity);
    }

    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#id))")
    @Transactional
    public Optional<UniversityDTO> updateUniversity(Long id, UniversityCreateUpdateDTO universityUpdateDTO) {
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isEmpty()) {
            return Optional.empty();
        }

        University university = universityOptional.get();
        university.setName(universityUpdateDTO.getName());
        university.setDescription(universityUpdateDTO.getDescription());
        university.setLocation(universityUpdateDTO.getLocation());
        university.setImageUrl(universityUpdateDTO.getImageUrl());
        university.setRequirements(universityUpdateDTO.getRequirements());

        University updatedUniversity = universityRepository.save(university);
        return Optional.of(convertToDTO(updatedUniversity));
    }

    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN')") // Only platform admin can delete universities for now
    @Transactional
    public boolean deleteUniversity(Long id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<UniversityDTO> getUniversitiesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<University> universityPage = universityRepository.findAll(pageable);
        return universityPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UniversityDTO> getUniversitiesPage(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<University> universityPage;
        if (name != null && !name.isEmpty()) {
            universityPage = universityRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            universityPage = universityRepository.findAll(pageable);
        }
        return universityPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // --- Security Helper Method ---
    // This method would be used in @PreAuthorize to check if a facility admin manages the given university.
    // This requires a link between University and the User who is the facility admin.
    // For example, if University entity has a `managedByUserId` field:
    public boolean canManageUniversity(Long universityId) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_FACILITY_ADMIN) {
            return false;
        }
        Optional<University> universityOptional = universityRepository.findById(universityId);
        return universityOptional.map(u ->
            u.getFacilityAdmin() != null && u.getFacilityAdmin().getId().equals(currentUser.getId())
        ).orElse(false);
    }

    // --- User Helper Method ---
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public University getCurrentFacilityAdminUniversity() {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_FACILITY_ADMIN) {
            throw new SecurityException("Not a facility admin user");
        }
        return universityRepository.findAll().stream()
            .filter(u -> u.getFacilityAdmin() != null && u.getFacilityAdmin().getId().equals(currentUser.getId()))
            .findFirst().orElseThrow(() -> new RuntimeException("No university assigned to this facility admin"));
    }

    public java.util.List<com.syalux.eduhub.model.University> getAssignedUniversitiesForStaff() {
        com.syalux.eduhub.model.User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_STAFF) {
            throw new SecurityException("Not a staff user");
        }
        if (currentUser.getAssignedUniversities() == null) return java.util.Collections.emptyList();
        return new java.util.ArrayList<>(currentUser.getAssignedUniversities());
    }

    public void assignFacilityAdminToUniversity(Long universityId, Long facilityAdminUserId) {
        University university = universityRepository.findById(universityId).orElseThrow();
        User facilityAdmin = userRepository.findById(facilityAdminUserId).orElseThrow();
        if (facilityAdmin.getRole() != com.syalux.eduhub.model.Role.ROLE_FACILITY_ADMIN) {
            throw new IllegalArgumentException("User is not a facility admin");
        }
        university.setFacilityAdmin(facilityAdmin);
        universityRepository.save(university);
    }

    @Transactional
    public University createUniversityForFacilityAdmin(String name, String description, String location, String imageUrl, java.util.List<String> requirements) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_FACILITY_ADMIN) {
            throw new SecurityException("Not a facility admin user");
        }
        University university = new University();
        university.setName(name);
        university.setDescription(description);
        university.setLocation(location);
        university.setImageUrl(imageUrl);
        university.setRequirements(requirements);
        university.setFacilityAdmin(currentUser);
        University saved = universityRepository.save(university);
        return saved;
    }
}

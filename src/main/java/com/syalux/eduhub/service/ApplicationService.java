package com.syalux.eduhub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.syalux.eduhub.dto.ApplicationCreateDTO;
import com.syalux.eduhub.dto.ApplicationDTO;
import com.syalux.eduhub.dto.ApplicationUpdateDTO;
import com.syalux.eduhub.model.*;
import com.syalux.eduhub.repository.ApplicationRepository;
import com.syalux.eduhub.repository.MajorRepository;
import com.syalux.eduhub.repository.UniversityRepository;
import com.syalux.eduhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UniversityService universityService; // For canManageUniversity checks

    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- DTO Converter ---
    public ApplicationDTO convertToDTO(Application application) {
        if (application == null) return null;
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        if (application.getStudent() != null) {
            dto.setStudentId(application.getStudent().getId());
            dto.setStudentUsername(application.getStudent().getUsername());
        }
        if (application.getUniversity() != null) {
            dto.setUniversityId(application.getUniversity().getId());
            dto.setUniversityName(application.getUniversity().getName());
        }
        if (application.getMajor() != null) {
            dto.setMajorId(application.getMajor().getId());
            dto.setMajorName(application.getMajor().getName());
        }
        dto.setStatus(application.getStatus());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());
        dto.setApplicationData(application.getApplicationData());
        return dto;
    }

    // --- Public Service Methods ---

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    public ApplicationDTO createApplication(ApplicationCreateDTO createDTO) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated.");
        }

        University university = universityRepository.findById(createDTO.getUniversityId())
                .orElseThrow(() -> new RuntimeException("University not found."));
        Major major = majorRepository.findById(createDTO.getMajorId())
                .orElseThrow(() -> new RuntimeException("Major not found."));

        if (!major.getUniversity().getId().equals(university.getId())) {
            throw new IllegalArgumentException("Major does not belong to the specified university.");
        }

        Application application = new Application();
        application.setStudent(currentUser);
        application.setUniversity(university);
        application.setMajor(major);
        application.setStatus(ApplicationStatus.IN_PROGRESS);
        application.setApplicationData(createDTO.getApplicationData() != null ? createDTO.getApplicationData() : "{}"); // Initialize with empty JSON if null
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());


        Application savedApplication = applicationRepository.save(application);
        return convertToDTO(savedApplication);
    }

    @PreAuthorize("(#applicationId != null && hasRole('ROLE_STUDENT') && @applicationService.isOwner(#applicationId))")
    @Transactional
    public Optional<ApplicationDTO> updateApplicationData(Long applicationId, ApplicationUpdateDTO updateDTO) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));

        // Only allow updates if application is IN_PROGRESS
        if (application.getStatus() != ApplicationStatus.IN_PROGRESS) {
            throw new IllegalStateException("Application cannot be updated as it's not in progress.");
        }

        try {
            // Assuming applicationDataPage contains a JSON string for a specific page/section
            // This will merge the new page data into the existing applicationData JSON
            JsonNode existingDataNode = objectMapper.readTree(application.getApplicationData() != null ? application.getApplicationData() : "{}");
            JsonNode newDataNode = objectMapper.readTree(updateDTO.getApplicationDataPage() != null ? updateDTO.getApplicationDataPage() : "{}");

            if (existingDataNode.isObject() && newDataNode.isObject()) {
                ObjectNode mergedNode = ((ObjectNode) existingDataNode).setAll((ObjectNode) newDataNode);
                application.setApplicationData(objectMapper.writeValueAsString(mergedNode));
            } else {
                // Fallback or error: if either is not an object, or if more complex merging is needed
                application.setApplicationData(updateDTO.getApplicationDataPage());
            }
            application.setUpdatedAt(LocalDateTime.now());
            Application updatedApplication = applicationRepository.save(application);
            return Optional.of(convertToDTO(updatedApplication));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing application data JSON.", e);
        }
    }


    @PreAuthorize("(#applicationId != null && (hasRole('ROLE_STUDENT') && @applicationService.isOwner(#applicationId)) or (hasRole('ROLE_FACILITY_ADMIN') and @applicationService.canFacilityAdminAccessApplication(#applicationId)) or hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF'))")
    @Transactional(readOnly = true)
    public Optional<ApplicationDTO> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId).map(this::convertToDTO);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getCurrentStudentApplications() {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated.");
        }
        return applicationRepository.findByStudentId(currentUser.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @PreAuthorize("(hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#universityId)) or hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByUniversity(Long universityId) {
        return applicationRepository.findByUniversityId(universityId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @PreAuthorize("(#applicationId != null && hasRole('ROLE_STUDENT') && @applicationService.isOwner(#applicationId) && #newStatus.name() == 'SUBMITTED' && @applicationService.canStudentSubmit(#applicationId))" +
      " or (#applicationId != null && hasRole('ROLE_STUDENT') && @applicationService.isOwner(#applicationId) && #newStatus.name() == 'WITHDRAWN' && @applicationService.canStudentWithdraw(#applicationId))" +
      " or (#applicationId != null && (hasRole('ROLE_FACILITY_ADMIN') and @applicationService.canFacilityAdminUpdateStatus(#applicationId, #newStatus)))" +
      " or (#applicationId != null && (hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')))") // Admins/Staff have more leeway
    @Transactional
    public Optional<ApplicationDTO> updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found."));

        // Add more specific logic here based on roles and allowed transitions
        // For example, a student can only move from IN_PROGRESS to SUBMITTED or to WITHDRAWN.
        // A facility admin can move from SUBMITTED to UNDER_REVIEW, ACCEPTED, REJECTED etc.

        application.setStatus(newStatus);
        application.setUpdatedAt(LocalDateTime.now());
        Application updatedApplication = applicationRepository.save(application);
        return Optional.of(convertToDTO(updatedApplication));
    }


    // --- Security Helper Methods ---

    public boolean isOwner(Long applicationId) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null) return false;
        return applicationRepository.findById(applicationId)
                .map(app -> app.getStudent().getId().equals(currentUser.getId()))
                .orElse(false);
    }

    public boolean canFacilityAdminAccessApplication(Long applicationId) {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != Role.ROLE_FACILITY_ADMIN) {
            return false;
        }
        return applicationRepository.findById(applicationId)
                .map(app -> universityService.canManageUniversity(app.getUniversity().getId()))
                .orElse(false);
    }
    
    public boolean canStudentSubmit(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .map(app -> app.getStatus() == ApplicationStatus.IN_PROGRESS)
                .orElse(false);
    }

    public boolean canStudentWithdraw(Long applicationId) {
        ApplicationStatus[] nonWithdrawableStatus = {ApplicationStatus.ACCEPTED, ApplicationStatus.REJECTED}; // Example
        return applicationRepository.findById(applicationId)
                .map(app -> {
                    for(ApplicationStatus status : nonWithdrawableStatus) {
                        if(app.getStatus() == status) return false;
                    }
                    return true; // Can withdraw if not in a final non-withdrawable state
                })
                .orElse(false);
    }

    public boolean canFacilityAdminUpdateStatus(Long applicationId, ApplicationStatus newStatus) {
        // Facility admin can only manage applications for their university
        if (!canFacilityAdminAccessApplication(applicationId)) {
            return false;
        }
        // Define allowed transitions for facility admin
        // e.g., from SUBMITTED to UNDER_REVIEW, ACCEPTED, REJECTED, WAITLISTED
        // e.g., from UNDER_REVIEW to ACCEPTED, REJECTED, WAITLISTED
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if(application == null) return false;

        ApplicationStatus currentStatus = application.getStatus();
        switch (currentStatus) {
            case SUBMITTED:
                return newStatus == ApplicationStatus.UNDER_REVIEW ||
                       newStatus == ApplicationStatus.ACCEPTED ||
                       newStatus == ApplicationStatus.REJECTED ||
                       newStatus == ApplicationStatus.WAITLISTED;
            case UNDER_REVIEW:
                return newStatus == ApplicationStatus.ACCEPTED ||
                       newStatus == ApplicationStatus.REJECTED ||
                       newStatus == ApplicationStatus.WAITLISTED;
            // Add more cases as needed
            default:
                return false; // By default, facility admin cannot change from other statuses
        }
    }

    // --- User Helper Method ---
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // --- Admin/Stats Methods ---
    public long countApplications() {
        return applicationRepository.count();
    }

    public String getTopUniversity() {
        // Simple implementation: find university with most applications
        List<Application> all = applicationRepository.findAll();
        return all.stream()
            .filter(a -> a.getUniversity() != null)
            .collect(Collectors.groupingBy(a -> a.getUniversity().getName(), Collectors.counting()))
            .entrySet().stream()
            .max((e1, e2) -> Long.compare(e1.getValue(), e2.getValue()))
            .map(e -> e.getKey())
            .orElse("N/A");
    }

    public List<Application> filterApplications(String university, String student, String status) {
        List<Application> all = applicationRepository.findAll();
        return all.stream()
            .filter(a -> university == null || university.isEmpty() || (a.getUniversity() != null && a.getUniversity().getName().toLowerCase().contains(university.toLowerCase())))
            .filter(a -> student == null || student.isEmpty() || (a.getStudent() != null && a.getStudent().getUsername().toLowerCase().contains(student.toLowerCase())))
            .filter(a -> status == null || status.isEmpty() || (a.getStatus() != null && a.getStatus().name().equalsIgnoreCase(status)))
            .collect(Collectors.toList());
    }

    public void changeStatus(Long appId, String status) {
        Application app = applicationRepository.findById(appId).orElse(null);
        if (app != null) {
            app.setStatus(com.syalux.eduhub.model.ApplicationStatus.valueOf(status));
            app.setUpdatedAt(java.time.LocalDateTime.now());
            applicationRepository.save(app);
        }
    }

    public void deleteApplication(Long appId) {
        applicationRepository.deleteById(appId);
    }

    public List<Application> getCurrentStudentApplicationsRaw() {
        User currentUser = authService.getLoggedInUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated.");
        }
        return applicationRepository.findByStudentId(currentUser.getId());
    }

    public Object getFacilityAdminStats(Long universityId) {
        // Example: return application counts by status for this university
        List<Application> apps = applicationRepository.findByUniversityId(universityId);
        Map<ApplicationStatus, Long> statusCounts = apps.stream()
            .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
        return statusCounts;
    }

    public Object getStaffStats() {
        com.syalux.eduhub.model.User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_STAFF) {
            throw new SecurityException("Not a staff user");
        }
        if (currentUser.getAssignedUniversities() == null || currentUser.getAssignedUniversities().isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        for (var uni : currentUser.getAssignedUniversities()) {
            long count = applicationRepository.findByUniversityId(uni.getId()).size();
            stats.put(uni.getName(), count);
        }
        return stats;
    }
    public java.util.List<Application> getStaffApplications() {
        com.syalux.eduhub.model.User currentUser = authService.getLoggedInUser();
        if (currentUser == null || currentUser.getRole() != com.syalux.eduhub.model.Role.ROLE_STAFF) {
            throw new SecurityException("Not a staff user");
        }
        if (currentUser.getAssignedUniversities() == null || currentUser.getAssignedUniversities().isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<Application> result = new java.util.ArrayList<>();
        for (var uni : currentUser.getAssignedUniversities()) {
            result.addAll(applicationRepository.findByUniversityId(uni.getId()));
        }
        return result;
    }

    public List<ApplicationDTO> filterApplicationsDTO(String university, String student, String status) {
        List<Application> all = applicationRepository.findAll();
        return all.stream()
            .filter(a -> university == null || university.isEmpty() || (a.getUniversity() != null && a.getUniversity().getName().toLowerCase().contains(university.toLowerCase())))
            .filter(a -> student == null || student.isEmpty() || (a.getStudent() != null && a.getStudent().getUsername().toLowerCase().contains(student.toLowerCase())))
            .filter(a -> status == null || status.isEmpty() || (a.getStatus() != null && a.getStatus().name().equalsIgnoreCase(status)))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}

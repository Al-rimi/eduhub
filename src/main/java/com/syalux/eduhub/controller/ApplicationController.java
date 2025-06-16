package com.syalux.eduhub.controller;

import com.syalux.eduhub.dto.ApplicationCreateDTO;
import com.syalux.eduhub.dto.ApplicationDTO;
import com.syalux.eduhub.dto.ApplicationUpdateDTO;
import com.syalux.eduhub.dto.MessageResponse;
import com.syalux.eduhub.model.ApplicationStatus;
import com.syalux.eduhub.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> createApplication(@RequestBody ApplicationCreateDTO createDTO) {
        try {
            ApplicationDTO createdApplication = applicationService.createApplication(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}/data")
    @PreAuthorize("hasRole('ROLE_STUDENT') and @applicationService.isOwner(#id)")
    public ResponseEntity<?> updateApplicationData(@PathVariable Long id, @RequestBody ApplicationUpdateDTO updateDTO) {
        try {
            return applicationService.updateApplicationData(id, updateDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) { // Catch only RuntimeException
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("(@applicationService.isOwner(#id) and hasRole('ROLE_STUDENT')) or " +
                  "(@applicationService.canFacilityAdminAccessApplication(#id) and hasRole('ROLE_FACILITY_ADMIN')) or " +
                  "hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<List<ApplicationDTO>> getCurrentStudentApplications() {
        List<ApplicationDTO> applications = applicationService.getCurrentStudentApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications(
            @RequestParam(required = false) Long universityId,
            @RequestParam(required = false) ApplicationStatus status) {
        List<ApplicationDTO> applications;
        if (universityId != null) {
            applications = applicationService.getApplicationsByUniversity(universityId);
        } else if (status != null) {
            applications = applicationService.getApplicationsByStatus(status);
        } else {
            applications = applicationService.getAllApplications();
        }
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/by-university/{universityId}")
    @PreAuthorize("(hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#universityId)) or hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByUniversity(@PathVariable Long universityId) {
        List<ApplicationDTO> applications = applicationService.getApplicationsByUniversity(universityId);
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("(hasRole('ROLE_STUDENT') and @applicationService.isOwner(#id)) or " +
                  "(hasRole('ROLE_FACILITY_ADMIN') and @applicationService.canFacilityAdminAccessApplication(#id)) or " +
                  "hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')")
    // More specific permission checks are inside the service method for status transitions
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        try {
            return applicationService.updateApplicationStatus(id, status)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) { // Catch only RuntimeException
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}

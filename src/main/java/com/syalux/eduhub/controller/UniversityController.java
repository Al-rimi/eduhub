package com.syalux.eduhub.controller;

import com.syalux.eduhub.dto.UniversityCreateUpdateDTO;
import com.syalux.eduhub.dto.UniversityDTO;
import com.syalux.eduhub.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow all origins for development
@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping
    public ResponseEntity<List<UniversityDTO>> getAllUniversities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<UniversityDTO> universities = universityService.getUniversitiesPage(page, size);
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityDTO> getUniversityById(@PathVariable Long id) {
        return universityService.getUniversityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_FACILITY_ADMIN')")
    public ResponseEntity<UniversityDTO> createUniversity(@RequestBody UniversityCreateUpdateDTO createDTO) {
        try {
            UniversityDTO createdUniversity = universityService.createUniversity(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUniversity);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            // Catch other specific exceptions if needed
            return ResponseEntity.badRequest().build(); // Or a DTO with error message
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#id))")
    public ResponseEntity<UniversityDTO> updateUniversity(@PathVariable Long id, @RequestBody UniversityCreateUpdateDTO updateDTO) {
        try {
            return universityService.updateUniversity(id, updateDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN')")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        try {
            if (universityService.deleteUniversity(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Controller
    @RequestMapping("/universities")
    public class UniversityDetailController {
        @Autowired
        private UniversityService universityService;

        @GetMapping("/{id}")
        public String universityDetail(@PathVariable Long id, Model model, Authentication authentication) {
            UniversityDTO university = universityService.getUniversityById(id).orElse(null);
            model.addAttribute("university", university);
            boolean isStudent = authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            model.addAttribute("isStudent", isStudent);
            return "university/detail";
        }
    }
}

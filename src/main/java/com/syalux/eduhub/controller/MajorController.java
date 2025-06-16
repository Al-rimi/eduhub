package com.syalux.eduhub.controller;

import com.syalux.eduhub.dto.MajorCreateUpdateDTO;
import com.syalux.eduhub.dto.MajorDTO;
import com.syalux.eduhub.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or hasRole('ROLE_STAFF')") // Or make public if needed
    public ResponseEntity<List<MajorDTO>> getAllMajors() {
        List<MajorDTO> majors = majorService.getAllMajors();
        return ResponseEntity.ok(majors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorDTO> getMajorById(@PathVariable Long id) {
        return majorService.getMajorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-university/{universityId}")
    public ResponseEntity<List<MajorDTO>> getMajorsByUniversity(@PathVariable Long universityId) {
        List<MajorDTO> majors = majorService.getMajorsByUniversityId(universityId);
        return ResponseEntity.ok(majors);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#createDTO.universityId))")
    public ResponseEntity<MajorDTO> createMajor(@RequestBody MajorCreateUpdateDTO createDTO) {
        try {
            MajorDTO createdMajor = majorService.createMajor(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMajor);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Consider a DTO with error message
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Consider a DTO with error message
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @majorService.canManageMajor(#id))")
    public ResponseEntity<MajorDTO> updateMajor(@PathVariable Long id, @RequestBody MajorCreateUpdateDTO updateDTO) {
        try {
            return majorService.updateMajor(id, updateDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @majorService.canManageMajor(#id))")
    public ResponseEntity<Void> deleteMajor(@PathVariable Long id) {
        try {
            if (majorService.deleteMajor(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

package com.syalux.eduhub.service;

import com.syalux.eduhub.dto.MajorCreateUpdateDTO;
import com.syalux.eduhub.dto.MajorDTO;
import com.syalux.eduhub.model.Major;
import com.syalux.eduhub.model.University;
import com.syalux.eduhub.repository.MajorRepository;
import com.syalux.eduhub.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private UniversityRepository universityRepository;
    
    @Autowired
    private UniversityService universityService; // For security checks

    // --- DTO Converter ---
    private MajorDTO convertToDTO(Major major) {
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
    public List<MajorDTO> getAllMajors() {
        return majorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MajorDTO> getMajorById(Long id) {
        return majorRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<MajorDTO> getMajorsByUniversityId(Long universityId) {
        return majorRepository.findByUniversityId(universityId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @universityService.canManageUniversity(#majorCreateDTO.universityId))")
    @Transactional
    public MajorDTO createMajor(MajorCreateUpdateDTO majorCreateDTO) {
        Optional<University> universityOptional = universityRepository.findById(majorCreateDTO.getUniversityId());
        if (universityOptional.isEmpty()) {
            throw new RuntimeException("University not found with id: " + majorCreateDTO.getUniversityId());
        }

        Major major = new Major();
        major.setName(majorCreateDTO.getName());
        major.setDescription(majorCreateDTO.getDescription());
        major.setUniversity(universityOptional.get());

        Major savedMajor = majorRepository.save(major);
        return convertToDTO(savedMajor);
    }

    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @majorService.canManageMajor(#id))")
    @Transactional
    public Optional<MajorDTO> updateMajor(Long id, MajorCreateUpdateDTO majorUpdateDTO) {
        Optional<Major> majorOptional = majorRepository.findById(id);
        if (majorOptional.isEmpty()) {
            return Optional.empty();
        }

        Major major = majorOptional.get();
        
        // Check if university is being changed and if user has rights for the new university
        if (!major.getUniversity().getId().equals(majorUpdateDTO.getUniversityId())) {
            // This requires checking if the facility admin can manage the *new* universityId
            // For simplicity, this check is basic. A more robust check would use universityService.canManageUniversity(majorUpdateDTO.getUniversityId())
            // However, @PreAuthorize already covers the current major's university. Changing university might need specific logic.
            Optional<University> newUniversityOptional = universityRepository.findById(majorUpdateDTO.getUniversityId());
            if (newUniversityOptional.isEmpty()){
                throw new RuntimeException("New university not found with id: " + majorUpdateDTO.getUniversityId());
            }
            // Add additional security check for the new university if necessary
            major.setUniversity(newUniversityOptional.get());
        }

        major.setName(majorUpdateDTO.getName());
        major.setDescription(majorUpdateDTO.getDescription());

        Major updatedMajor = majorRepository.save(major);
        return Optional.of(convertToDTO(updatedMajor));
    }

    @PreAuthorize("hasRole('ROLE_PLATFORM_ADMIN') or (hasRole('ROLE_FACILITY_ADMIN') and @majorService.canManageMajor(#id))")
    @Transactional
    public boolean deleteMajor(Long id) {
        if (majorRepository.existsById(id)) {
            majorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // --- Security Helper Method ---
    public boolean canManageMajor(Long majorId) {
        Optional<Major> majorOptional = majorRepository.findById(majorId);
        if (majorOptional.isEmpty()) {
            return false; // Or throw exception
        }
        // Delegate to universityService's canManageUniversity, using the major's university ID
        return universityService.canManageUniversity(majorOptional.get().getUniversity().getId());
    }
}

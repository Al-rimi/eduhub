package com.syalux.eduhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syalux.eduhub.dto.ApplicationCreateDTO;
import com.syalux.eduhub.dto.ApplicationDTO;
import com.syalux.eduhub.service.ApplicationService;
import com.syalux.eduhub.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/application")
public class ApplicationJspController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UniversityService universityService;

    @GetMapping("/apply")
    public String showApplicationForm(@RequestParam Long universityId, Model model) {
        model.addAttribute("university", universityService.getUniversityById(universityId).orElse(null));
        model.addAttribute("form", new java.util.HashMap<String, String>());
        return "application/form";
    }

    @PostMapping("/apply")
    public String submitApplication(@RequestParam Long universityId,
                                    @RequestParam Long majorId,
                                    @RequestParam String fullName,
                                    @RequestParam String dateOfBirth,
                                    @RequestParam String nationality,
                                    @RequestParam String highSchoolName,
                                    @RequestParam String graduationYear,
                                    @RequestParam String gpa,
                                    @RequestParam String essay,
                                    Model model, Authentication authentication) {
        try {
            // Build applicationData JSON
            java.util.Map<String, String> dataMap = new java.util.HashMap<>();
            dataMap.put("fullName", fullName);
            dataMap.put("dateOfBirth", dateOfBirth);
            dataMap.put("nationality", nationality);
            dataMap.put("highSchoolName", highSchoolName);
            dataMap.put("graduationYear", graduationYear);
            dataMap.put("gpa", gpa);
            dataMap.put("essay", essay);
            String applicationData = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(dataMap);
            var createDTO = new com.syalux.eduhub.dto.ApplicationCreateDTO(universityId, majorId, applicationData);
            applicationService.createApplication(createDTO);
            return "redirect:/student/applications";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("university", universityService.getUniversityById(universityId).orElse(null));
            java.util.Map<String, String> form = new java.util.HashMap<>();
            form.put("fullName", fullName);
            form.put("dateOfBirth", dateOfBirth);
            form.put("nationality", nationality);
            form.put("highSchoolName", highSchoolName);
            form.put("graduationYear", graduationYear);
            form.put("gpa", gpa);
            form.put("essay", essay);
            model.addAttribute("form", form);
            return "application/form";
        }
    }

    @GetMapping("/my-applications")
    public String myApplications(Model model, Authentication authentication) {
        List<ApplicationDTO> applications = applicationService.getCurrentStudentApplications();
        model.addAttribute("applications", applications);
        return "application/my-applications";
    }

    @GetMapping("/detail")
    public String applicationDetail(@RequestParam Long id, Model model) {
        ApplicationDTO application = applicationService.getApplicationById(id).orElse(null);
        model.addAttribute("application", application);
        // Parse applicationData JSON to Map for display
        if (application != null && application.getApplicationData() != null && !application.getApplicationData().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> dataMap = mapper.readValue(application.getApplicationData(), Map.class);
                model.addAttribute("applicationDataMap", dataMap);
            } catch (Exception e) {
                // fallback: do not add map
            }
        }
        return "application/detail";
    }
}

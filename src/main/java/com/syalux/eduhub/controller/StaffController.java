package com.syalux.eduhub.controller;

import com.syalux.eduhub.service.ApplicationService;
import com.syalux.eduhub.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasRole('STAFF')")
public class StaffController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UniversityService universityService;

    @GetMapping("/home")
    public String home(Model model) {
        // TODO: Add logic to fetch assigned universities and stats
        model.addAttribute("universities", universityService.getAssignedUniversitiesForStaff());
        model.addAttribute("stats", applicationService.getStaffStats());
        return "staff/home";
    }

    @GetMapping("/applications")
    public String applications(Model model) {
        // Use DTOs for staff applications
        var applications = applicationService.getStaffApplications().stream()
            .map(app -> applicationService.convertToDTO(app))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("applications", applications);
        return "staff/applications";
    }

    @PostMapping("/applications/status")
    public String changeStatus(@RequestParam Long appId, @RequestParam String status) {
        applicationService.changeStatus(appId, status);
        return "redirect:/staff/applications";
    }

    @PostMapping("/applications/delete")
    public String deleteApplication(@RequestParam Long appId) {
        applicationService.deleteApplication(appId);
        return "redirect:/staff/applications";
    }

    @GetMapping("/applications/view")
    public String viewApplication(@RequestParam Long id, Model model) {
        var application = applicationService.getApplicationById(id).orElse(null);
        model.addAttribute("application", application);
        // Parse applicationData JSON to Map for display
        if (application != null && application.getApplicationData() != null && !application.getApplicationData().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> dataMap = mapper.readValue(application.getApplicationData(), java.util.Map.class);
                model.addAttribute("applicationDataMap", dataMap);
            } catch (Exception e) {
                // fallback: do not add map
            }
        }
        return "application/detail";
    }
}

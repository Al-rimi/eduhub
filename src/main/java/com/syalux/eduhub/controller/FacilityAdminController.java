package com.syalux.eduhub.controller;

import com.syalux.eduhub.model.University;
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
import java.util.List;

@Controller
@RequestMapping("/facility-admin")
@PreAuthorize("hasRole('FACILITY_ADMIN')")
public class FacilityAdminController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UniversityService universityService;

    @GetMapping("/home")
    public String home(Model model) {
        try {
            University uni = universityService.getCurrentFacilityAdminUniversity();
            model.addAttribute("university", uni);
            model.addAttribute("stats", applicationService.getFacilityAdminStats(uni.getId()));
            return "facility-admin/home";
        } catch (RuntimeException e) {
            // Show registration form if no university assigned
            return "facility-admin/register-university";
        }
    }

    @GetMapping("/applications")
    public String applications(Model model) {
        try {
            University uni = universityService.getCurrentFacilityAdminUniversity();
            List<com.syalux.eduhub.dto.ApplicationDTO> applications = applicationService.getApplicationsByUniversity(uni.getId());
            model.addAttribute("applications", applications);
            return "facility-admin/applications";
        } catch (RuntimeException e) {
            model.addAttribute("error", "No university assigned to this facility admin.");
            return "facility-admin/error";
        }
    }

    @PostMapping("/applications/status")
    public String changeStatus(@RequestParam Long appId, @RequestParam String status) {
        applicationService.changeStatus(appId, status);
        return "redirect:/facility-admin/applications";
    }

    @PostMapping("/applications/delete")
    public String deleteApplication(@RequestParam Long appId) {
        applicationService.deleteApplication(appId);
        return "redirect:/facility-admin/applications";
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

    @GetMapping("/register-university")
    public String showRegisterUniversityForm() {
        return "facility-admin/register-university";
    }

    @PostMapping("/register-university")
    public String registerUniversity(@RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam String location,
                                     @RequestParam(required = false) String imageUrl,
                                     @RequestParam(required = false) String requirements) {
        // Parse requirements
        java.util.List<String> reqList = new java.util.ArrayList<>();
        if (requirements != null && !requirements.trim().isEmpty()) {
            for (String req : requirements.split(",")) {
                reqList.add(req.trim());
            }
        }
        universityService.createUniversityForFacilityAdmin(name, description, location, imageUrl, reqList);
        return "redirect:/facility-admin/home";
    }
}

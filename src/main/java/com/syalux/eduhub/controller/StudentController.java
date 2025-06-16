package com.syalux.eduhub.controller;

import com.syalux.eduhub.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    @Autowired
    private ApplicationService applicationService;

    // Remove student home page and redirect all student home requests to the main home page
    @GetMapping("/home")
    public String studentHomeRedirect() {
        return "redirect:/home";
    }

    @GetMapping("/applications")
    public String myApplications(Model model) {
        var applications = applicationService.getCurrentStudentApplications();
        model.addAttribute("applications", applications);
        return "student/applications";
    }

    @PostMapping("/applications/delete")
    public String deleteApplication(@RequestParam Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return "redirect:/student/applications";
    }

    @GetMapping("/applications/edit")
    public String editApplicationForm(@RequestParam Long id, Model model) {
        var application = applicationService.getApplicationById(id).orElse(null);
        if (application == null) {
            model.addAttribute("error", "Application not found.");
            return "student/applications";
        }
        model.addAttribute("application", application);
        // Parse applicationData JSON to Map for display in the form
        if (application.getApplicationData() != null && !application.getApplicationData().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> dataMap = mapper.readValue(application.getApplicationData(), java.util.Map.class);
                model.addAttribute("applicationDataMap", dataMap);
            } catch (Exception e) {
                // fallback: do not add map
            }
        }
        return "student/edit-application";
    }

    @PostMapping("/applications/edit")
    public String editApplicationSubmit(@RequestParam Long id, @RequestParam java.util.Map<String, String> allParams, Model model) {
        // Build a map of application data fields from the form
        java.util.Map<String, Object> dataMap = new java.util.HashMap<>();
        for (var entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("data_")) {
                String key = entry.getKey().substring(5);
                dataMap.put(key, entry.getValue());
            }
        }
        String jsonData = "{}";
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            jsonData = mapper.writeValueAsString(dataMap);
        } catch (Exception e) {
            // fallback: keep as empty JSON
        }
        var updateDTO = new com.syalux.eduhub.dto.ApplicationUpdateDTO();
        updateDTO.setApplicationDataPage(jsonData);
        applicationService.updateApplicationData(id, updateDTO);
        return "redirect:/student/applications";
    }
}

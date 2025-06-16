package com.syalux.eduhub.controller;

import com.syalux.eduhub.model.User;
import com.syalux.eduhub.model.University;
import com.syalux.eduhub.service.UserService;
import com.syalux.eduhub.service.ApplicationService;
import com.syalux.eduhub.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UniversityService universityService;

    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalApplications", applicationService.countApplications());
        model.addAttribute("topUniversity", applicationService.getTopUniversity());
        // Add chart data as needed
        return "admin/home";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/role")
    public String changeUserRole(@RequestParam Long userId, @RequestParam String role) {
        userService.changeUserRole(userId, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/applications")
    public String applicationsPage(@RequestParam(required = false) String university,
                                   @RequestParam(required = false) String student,
                                   @RequestParam(required = false) String status,
                                   Model model) {
        // Use DTO-based filtering for admin applications page
        var applications = applicationService.filterApplicationsDTO(university, student, status);
        model.addAttribute("applications", applications);
        return "admin/applications";
    }

    @PostMapping("/applications/status")
    public String changeApplicationStatus(@RequestParam Long appId, @RequestParam String status) {
        applicationService.changeStatus(appId, status);
        return "redirect:/admin/applications";
    }

    @PostMapping("/applications/delete")
    public String deleteApplication(@RequestParam Long appId) {
        applicationService.deleteApplication(appId);
        return "redirect:/admin/applications";
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

    @GetMapping("/staff-university")
    public String staffUniversityPage(Model model) {
        model.addAttribute("staffList", userService.getAllStaff());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin/staff-university";
    }

    @PostMapping("/staff-university/assign")
    public String assignUniversityToStaff(@RequestParam Long staffId, @RequestParam Long universityId) {
        userService.assignUniversityToStaff(staffId, universityId);
        return "redirect:/admin/staff-university";
    }

    @PostMapping("/staff-university/remove")
    public String removeUniversityFromStaff(@RequestParam Long staffId, @RequestParam Long universityId) {
        userService.removeUniversityFromStaff(staffId, universityId);
        return "redirect:/admin/staff-university";
    }

    @PostMapping("/universities/assign-facility-admin")
    public String assignFacilityAdmin(@RequestParam Long universityId, @RequestParam Long facilityAdminUserId) {
        universityService.assignFacilityAdminToUniversity(universityId, facilityAdminUserId);
        return "redirect:/admin/universities";
    }
}

package com.syalux.eduhub.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(Model model, Authentication authentication) {
        // Add any student-specific data to the model
        return "dashboard/student";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public String adminDashboard(Model model, Authentication authentication) {
        // Add admin-specific data to the model
        return "dashboard/admin";
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public String staffDashboard(Model model, Authentication authentication) {
        // Add staff-specific data to the model
        return "dashboard/staff";
    }

    @GetMapping("/facility-admin")
    @PreAuthorize("hasRole('FACILITY_ADMIN')")
    public String facilityAdminDashboard(Model model, Authentication authentication) {
        // Add facility admin-specific data to the model
        return "dashboard/facility-admin";
    }
}

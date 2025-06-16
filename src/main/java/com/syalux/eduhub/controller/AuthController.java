package com.syalux.eduhub.controller;

import com.syalux.eduhub.dto.LoginRequest;
import com.syalux.eduhub.dto.MessageResponse;
import com.syalux.eduhub.dto.RegisterRequest;
import com.syalux.eduhub.model.User;
import com.syalux.eduhub.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = authService.registerUser(registerRequest);
            // Return user info (excluding sensitive data)
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authService.authenticateUser(loginRequest);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = authService.getLoggedInUser();

            if (user == null) {
                return ResponseEntity.status(401).body(new MessageResponse("Error: Could not retrieve user details after login."));
            }

            return ResponseEntity.ok(new MessageResponse("User logged in successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new MessageResponse("Error: Invalid username or password."));
        }
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext(); // Clear security context
            return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error during logout: " + e.getMessage()));
        }
    }

    @GetMapping("/api/auth/user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // Logic to get the currently logged-in user without JWT
        User user = authService.getLoggedInUser();
        if (user == null) {
            return ResponseEntity.status(401).body(new MessageResponse("User not found."));
        }
        return ResponseEntity.ok(new MessageResponse("User details retrieved successfully!"));
    }
}

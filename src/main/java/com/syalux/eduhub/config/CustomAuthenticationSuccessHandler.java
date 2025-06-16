package com.syalux.eduhub.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Check for saved request (originally requested page)
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) requestCache.getRequest(request, response);
        String redirectUrl = null;
        boolean isStudent = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_STUDENT")) {
                isStudent = true;
                break;
            }
        }
        if (savedRequest != null && isStudent) {
            String targetUrl = savedRequest.getRedirectUrl();
            // Only allow redirect to valid app URLs
            if (targetUrl != null && targetUrl.startsWith(request.getContextPath()) && !targetUrl.contains(".well-known")) {
                redirectUrl = targetUrl;
            }
        }
        if (redirectUrl == null) {
            // Role-based home fallback
            redirectUrl = "/";
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_PLATFORM_ADMIN")) {
                    redirectUrl = "/admin/home";
                    break;
                } else if (role.equals("ROLE_STAFF")) {
                    redirectUrl = "/staff/home";
                    break;
                } else if (role.equals("ROLE_FACILITY_ADMIN")) {
                    redirectUrl = "/facility-admin/home";
                    break;
                } else if (role.equals("ROLE_STUDENT")) {
                    redirectUrl = "/student/home";
                    break;
                }
            }
        }
        response.sendRedirect(redirectUrl);
    }
}

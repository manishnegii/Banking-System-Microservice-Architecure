package com.rexchord.common_security.util;

import com.rexchord.common_security.model.CustomUserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@UtilityClass
public class SecurityUtils {

    public CustomUserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserPrincipal user) {
            return user;
        }

        return null;
    }

    public Long getCurrentUserId() {
        CustomUserPrincipal user = getCurrentUser();
        return user != null ? Long.valueOf(user.getUserId()) : null;
    }

    public String getCurrentUserEmail() {
        CustomUserPrincipal user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    public String getRoles() {
        CustomUserPrincipal user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    public boolean hasRole(String role) {
        CustomUserPrincipal user = getCurrentUser();
        return user != null && user.getRole() != null && user.getRole().contains(role);
    }
}
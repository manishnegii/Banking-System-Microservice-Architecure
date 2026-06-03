package com.spring.user_service.config;

import com.rexchord.common_security.model.CustomUserPrincipal;
import com.spring.user_service.entity.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class SecurityContext {

    public static Long getCurrentUserId(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)){
            return null;
        }
        return Long.valueOf(principal.getUserId());
    }

    public static Role getRole(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)){
            return null;
        }

        String role = principal.getRole();
        if(role == null){
            return null;
        }
        return Role.valueOf(role);
    }
}



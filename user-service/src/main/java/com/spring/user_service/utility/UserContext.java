package com.spring.user_service.utility;

import com.spring.user_service.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContext {

    private final HttpServletRequest request;

    public Long getUserId(){

        String userId =
                request.getHeader("X-User-Id");

        if(userId == null){
            throw new RuntimeException(
                    "User Id header missing"
            );
        }
        return Long.valueOf(userId);
    }

    public Role getRole(){
        String role =
                request.getHeader("X-Role");

        if(role == null){
            throw new RuntimeException(
                    "Role header missing"
            );
        }
        return Role.valueOf(
                role.toUpperCase()
        );
    }
}

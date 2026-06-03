package com.spring.auth_service.dto;

import com.spring.auth_service.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean isActive;
    private Boolean emailVerified;
    private Boolean accountNonLocked;
    private Integer failedAttempts;
    private LocalDateTime lastLoginAt;
    private LocalDateTime passwordChangedAt;

}

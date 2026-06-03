package com.spring.auth_service.dto;

import com.spring.auth_service.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserUpdateRequestDto {

    @Email
    private String email;

    private Role role;

    private Boolean isActive;

    private Boolean emailVerified;

    private Boolean accountNonLocked;

    @Size(min = 8)
    private String password;
}

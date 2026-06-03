package com.spring.auth_service.admin;

import com.spring.auth_service.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}

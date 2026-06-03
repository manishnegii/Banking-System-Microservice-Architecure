package com.spring.auth_service.admin;

import com.spring.auth_service.entity.Role;
import lombok.Data;

@Data
public class AssignRoleRequest {
    private Long id;
    private Role role;
}

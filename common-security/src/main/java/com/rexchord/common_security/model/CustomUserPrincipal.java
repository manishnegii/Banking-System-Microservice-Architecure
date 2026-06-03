package com.rexchord.common_security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserPrincipal {
    public String userId;
    public String email;
    public String role;
}

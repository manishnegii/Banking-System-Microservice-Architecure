package com.spring.auth_service.config;


import com.spring.auth_service.entity.Role;

public class SecurityContext {

    private static final ThreadLocal<Role> ROLE_HOLDER = new ThreadLocal<>();

    public static void setRole(Role role){
        ROLE_HOLDER.set(role);
    }

    public static Role getRole(){
        return ROLE_HOLDER.get();
    }

    public static void clear(){
        ROLE_HOLDER.remove();
    }
}
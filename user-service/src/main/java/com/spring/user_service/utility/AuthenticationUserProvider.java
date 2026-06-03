//package com.spring.user_service.utility;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AuthenticationUserProvider {
//    public Long getCurrentUserId(){
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (Long) authentication.getPrincipal();
//    }
//}

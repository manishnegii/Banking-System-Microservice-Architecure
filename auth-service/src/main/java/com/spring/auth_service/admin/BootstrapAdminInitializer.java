package com.spring.auth_service.admin;

import com.spring.auth_service.entity.AuthUser;
import com.spring.auth_service.entity.Role;
import com.spring.auth_service.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapAdminInitializer implements CommandLineRunner {

    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository userRepository;

    @Override
    public void run(String... args){
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if(!adminExists){
            var admin = new AuthUser();
            admin.setUsername(adminConfig.getAdminName());
            admin.setEmail(adminConfig.getAdminName()+"@domain.com");
            admin.setPassword(passwordEncoder.encode(adminConfig.getAdminPassword()));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }
}

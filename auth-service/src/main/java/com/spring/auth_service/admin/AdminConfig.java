package com.spring.auth_service.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bootstrap.admin")
public class AdminConfig {
    private String adminName;
    private String adminPassword;

}
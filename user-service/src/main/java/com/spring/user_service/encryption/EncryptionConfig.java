package com.spring.user_service.encryption;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.encryption")
public class EncryptionConfig {

    private String activeKeyVersion;

    private Map<String, String> keys = new HashMap<>();
}



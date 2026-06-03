package com.spring.user_service.encryption;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class KeyManagerService {

    private final EncryptionConfig config;

    private final Map<String, SecretKey> cachedKeys =
            new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

        config.getKeys().forEach((version, value) -> {

            byte[] decoded = Base64.getDecoder().decode(value);

            SecretKey key = new SecretKeySpec(decoded, "AES");

            cachedKeys.put(version, key);
        });
    }

    public SecretKey getKey(String version) {
        return cachedKeys.get(version);
    }

    public String getActiveVersion() {
        return config.getActiveKeyVersion();
    }
}



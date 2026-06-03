package com.spring.user_service.encryption.hash;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class Sha256Utils {

    public static String hash(String value) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashed);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}



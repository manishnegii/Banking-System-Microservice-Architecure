package com.spring.user_service.encryption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AesEncryptionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private final KeyManagerService keyManagerService;

    public String encrypt(String plainText, String keyVersion
    ) {

        try {

            SecretKey secretKey = keyManagerService.getKey(keyVersion);

            byte[] iv = new byte[IV_LENGTH];

            SecureRandom secureRandom = new SecureRandom();

            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    new GCMParameterSpec(TAG_LENGTH, iv)
            );

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);

            buffer.put(iv);
            buffer.put(encrypted);

            return Base64.getEncoder()
                    .encodeToString(buffer.array());

        } catch (Exception ex) {

            throw new EncryptionException(
                    "Failed to encrypt",
                    ex
            );
        }
    }

    public String decrypt(
            String encryptedText,
            String keyVersion
    ) {

        try {

            SecretKey secretKey = keyManagerService.getKey(keyVersion);

            byte[] decoded = Base64.getDecoder().decode(encryptedText);

            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[IV_LENGTH];

            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];

            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    new GCMParameterSpec(TAG_LENGTH, iv)
            );

            byte[] decrypted = cipher.doFinal(cipherText);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception ex) {

            throw new EncryptionException(
                    "Failed to decrypt",
                    ex
            );
        }
    }
}



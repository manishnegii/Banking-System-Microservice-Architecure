package com.spring.user_service.encryption;

public class EncryptionException extends RuntimeException {
    public EncryptionException(String message,Throwable cause) {
        super(message,cause);
    }
}

package com.spring.transaction_service.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    protected CustomException(String message) {
        super(message);
    }

    public abstract String getErrorCode();

    public abstract HttpStatus getStatus();
}

package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    public abstract String getErrorCode();

    public abstract HttpStatus getStatus();
}

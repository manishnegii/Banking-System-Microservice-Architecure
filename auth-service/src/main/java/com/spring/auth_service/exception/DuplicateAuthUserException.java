package com.spring.auth_service.exception;

import com.spring.auth_service.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateAuthUserException extends CustomException {

    @Override
    public String getErrorCode() {
        return "DUPLICATE_AUTH_USER";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

package com.spring.auth_service.exception;

import com.spring.auth_service.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AuthUserNotFoundException extends CustomException {

    @Override
    public String getErrorCode() {
        return "AUTH_USER_NOT_FOUND";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

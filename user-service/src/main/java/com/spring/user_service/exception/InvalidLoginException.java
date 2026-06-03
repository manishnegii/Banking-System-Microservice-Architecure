package com.spring.user_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class InvalidLoginException extends CustomException {

    private final String message;

    private final Integer failedAttempt;

    @Override
    public String getErrorCode() {
        return "INVALID_CREDENTIALS";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

}

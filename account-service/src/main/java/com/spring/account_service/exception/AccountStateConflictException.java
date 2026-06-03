package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class AccountStateConflictException extends CustomException {

    private static final String ERROR_CODE = "ACCOUNT_STATE_CONFLICT";

    public AccountStateConflictException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

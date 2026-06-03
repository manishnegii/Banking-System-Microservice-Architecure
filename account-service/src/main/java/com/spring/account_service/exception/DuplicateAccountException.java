package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateAccountException extends CustomException {

    private static final String ERROR_CODE = "DUPLICATE_ACCOUNT_NUMBER";

    public DuplicateAccountException() {
        super("Account number already exists");
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

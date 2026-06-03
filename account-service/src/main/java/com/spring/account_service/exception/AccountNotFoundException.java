package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends CustomException {

    private static final String ERROR_CODE = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException() {
        super("Account not found");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

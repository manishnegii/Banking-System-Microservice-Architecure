package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class AccountAlreadyExists extends CustomException {

    private static final String ERROR_CODE = "ACCOUNT_ALREDY_EXISTS";

    public AccountAlreadyExists() {
        super("Account Already Exists");
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
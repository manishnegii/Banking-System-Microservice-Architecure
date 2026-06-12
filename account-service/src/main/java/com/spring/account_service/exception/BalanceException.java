package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class BalanceException extends CustomException {
    private static final String ERROR_CODE = "INSUFFICIENT_BALANCE";

    public BalanceException() {
        super("Insufficient balance");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class ClosedAccountOperationException extends CustomException {

    private static final String ERROR_CODE = "CLOSED_ACCOUNT_OPERATION";

    public ClosedAccountOperationException() {
        super("Operation not allowed on closed account");
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

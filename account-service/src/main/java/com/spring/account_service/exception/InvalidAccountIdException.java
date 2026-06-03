package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidAccountIdException extends CustomException {

    private static final String ERROR_CODE = "INVALID_ACCOUNT_ID";

    public InvalidAccountIdException() {
        super("Invalid accountId");
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

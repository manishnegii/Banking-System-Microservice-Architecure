package com.spring.transaction_service.exception;

import org.springframework.http.HttpStatus;

public class TransactionFailedException extends CustomException {
    private static final String ERROR_CODE = "TRANSACTION_FAILED";

    public TransactionFailedException(String message) {
        super(message);
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

package com.spring.transaction_service.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends CustomException {

    private static final String ERROR_CODE = "TRANSCATION_NOT_FOUND";

    public TransactionNotFoundException() {
        super("Transaction not found");
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

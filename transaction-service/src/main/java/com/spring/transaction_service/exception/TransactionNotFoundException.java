package com.spring.transaction_service.exception;

public class TransactionNotFoundException extends RuntimeException {

    private String errorCode;

    public TransactionNotFoundException(String message) {
        super(message);
        this.errorCode = "TXN_NOT_FOUND";
    }

    public TransactionNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

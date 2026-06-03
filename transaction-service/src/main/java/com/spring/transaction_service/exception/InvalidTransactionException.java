package com.spring.transaction_service.exception;

public class InvalidTransactionException extends RuntimeException {

    private String errorCode;

    public InvalidTransactionException(String message) {
        super(message);
        this.errorCode = "INVALID_TXN";
    }

    public InvalidTransactionException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

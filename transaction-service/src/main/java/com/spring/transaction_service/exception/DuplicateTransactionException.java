package com.spring.transaction_service.exception;

public class DuplicateTransactionException extends RuntimeException {

    private String errorCode;

    public DuplicateTransactionException(String message) {
        super(message);
        this.errorCode = "DUPLICATE_TXN";
    }

    public DuplicateTransactionException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

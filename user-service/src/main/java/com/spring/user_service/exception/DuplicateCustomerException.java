package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateCustomerException extends CustomException {
    @Override
    public String getErrorCode() {
        return "CUSTOMER_ALREADY_EXISTS";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

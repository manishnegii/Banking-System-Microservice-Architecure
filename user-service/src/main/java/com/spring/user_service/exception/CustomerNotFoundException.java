package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends CustomException {

    @Override
    public String getErrorCode() {
        return "CUSTOMER_NOT_FOUND";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

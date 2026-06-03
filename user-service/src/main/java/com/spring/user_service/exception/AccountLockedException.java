package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends CustomException{
    @Override
    public String getErrorCode() {
        return "ACCOUNT_TEMPORARY_LOCKED";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.LOCKED;
    }
}

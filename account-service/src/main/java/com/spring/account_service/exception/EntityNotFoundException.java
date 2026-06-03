package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends CustomException {
    private static final String ERROR_CODE = "BRANCH_NOT_FOUND";

    public EntityNotFoundException(String message){
        super(message);
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

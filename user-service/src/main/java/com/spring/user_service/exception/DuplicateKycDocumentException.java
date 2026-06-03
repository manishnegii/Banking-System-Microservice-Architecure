package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateKycDocumentException extends CustomException {

    @Override
    public String getErrorCode() {
        return "DUPLICATE_KYC_DOCUMENT";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

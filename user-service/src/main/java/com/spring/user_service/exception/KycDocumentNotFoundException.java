package com.spring.user_service.exception;

import org.springframework.http.HttpStatus;

public class KycDocumentNotFoundException extends CustomException {
    @Override
    public String getErrorCode() {
        return "KYC_DOCUMENT_NOT_FOUND";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

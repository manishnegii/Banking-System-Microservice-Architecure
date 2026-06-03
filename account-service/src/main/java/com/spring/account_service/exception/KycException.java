package com.spring.account_service.exception;

import org.springframework.http.HttpStatus;

public class KycException extends CustomException {

    private static final String ERROR_CODE = "KYC_NOT_VERIFIED";

    public KycException() {
        super("Kyc not Verified");
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}

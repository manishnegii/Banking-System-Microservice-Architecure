package com.spring.account_service.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;
}

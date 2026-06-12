package com.spring.transaction_service.entity;

public enum TransactionalStatus {
    PENDING,
    RETRYING,
    SUCCESS,
    COMPENSATED,
    FAILED,
    PROCESSING,
    COMPLETED
}

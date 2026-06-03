package com.spring.account_service.client.dto;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long customerId;
    private String kycStatus;
}

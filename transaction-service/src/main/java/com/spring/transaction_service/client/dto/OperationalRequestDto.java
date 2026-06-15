package com.spring.transaction_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationalRequestDto {
    private String txnId;
    private String accountNumber;
    private BigDecimal amount;
}

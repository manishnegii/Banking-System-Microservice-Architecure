package com.spring.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationalRequestDto {
    private String txnId;
    private Long accountNumber;
    private BigDecimal amount;
}

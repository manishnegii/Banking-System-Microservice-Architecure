package com.spring.transaction_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private Long accountId;
    private Long userId;
    private String accountNumber;
    private BigDecimal balance;
}

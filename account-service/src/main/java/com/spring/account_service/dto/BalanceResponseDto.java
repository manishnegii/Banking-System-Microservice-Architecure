package com.spring.account_service.dto;

import com.spring.account_service.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceResponseDto {
    private Long accountId;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
}


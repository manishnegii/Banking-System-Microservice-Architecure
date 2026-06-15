package com.spring.transaction_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private String transactionId;

    private Long userId;

    private String fromAccountNumber;

    private String toAccountNumber;

    private BigDecimal amount;

    private String transactionType;

    private String status;

    private String idempotencyKey;

    private String failureReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<PaymentGatewayLogsDTO> paymentGatewayLogs;
}

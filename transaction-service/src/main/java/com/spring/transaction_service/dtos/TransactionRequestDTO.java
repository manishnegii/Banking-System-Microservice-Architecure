package com.spring.transaction_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "From Account ID is required")
    private Long fromAccountId;

    @NotNull(message = "To Account ID is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Transaction type is required")
    private String transactionType;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String merchantId;

    private String upiId;

    @NotBlank(message = "Idempotency key is required")
    private String idempotencyKey;

    private String gatewayTxnId;
}

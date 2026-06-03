package com.spring.transaction_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGatewayLogsDTO {

    private Long logId;

    private Long txnId;

    private String gatewayName;

    private String requestPayload;

    private String responsePayload;

    private String gatewayStatus;

    private LocalDateTime createdAt;
}

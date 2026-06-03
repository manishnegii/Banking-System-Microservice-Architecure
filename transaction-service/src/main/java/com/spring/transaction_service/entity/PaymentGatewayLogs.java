package com.spring.transaction_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_gateway_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGatewayLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "txn_id", nullable = false)
    private Long txnId;

    @Column(name = "gateway_name", nullable = false)
    private String gatewayName;

    @Column(name = "request_payload", columnDefinition = "LONGTEXT")
    private String requestPayload;

    @Column(name = "response_payload", columnDefinition = "LONGTEXT")
    private String responsePayload;

    @Column(name = "gateway_status", nullable = false)
    private String gatewayStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "txn_id", referencedColumnName = "transaction_id", insertable = false, updatable = false)
    @JsonBackReference
    private Transaction transaction;

}

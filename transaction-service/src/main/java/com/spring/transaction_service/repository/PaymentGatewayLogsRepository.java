package com.spring.transaction_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.spring.transaction_service.entity.PaymentGatewayLogs;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentGatewayLogsRepository extends JpaRepository<PaymentGatewayLogs, Long> {

    @Query("SELECT pgl FROM PaymentGatewayLogs pgl WHERE pgl.txnId = :txnId ORDER BY pgl.createdAt DESC")
    List<PaymentGatewayLogs> findByTxnId(@Param("txnId") Long txnId);

    @Query("SELECT pgl FROM PaymentGatewayLogs pgl WHERE pgl.gatewayName = :gatewayName ORDER BY pgl.createdAt DESC")
    List<PaymentGatewayLogs> findByGatewayName(@Param("gatewayName") String gatewayName);

    @Query("SELECT pgl FROM PaymentGatewayLogs pgl WHERE pgl.gatewayStatus = :status ORDER BY pgl.createdAt DESC")
    List<PaymentGatewayLogs> findByGatewayStatus(@Param("status") String status);

    Optional<PaymentGatewayLogs> findFirstByTxnIdOrderByCreatedAtDesc(Long txnId);
}

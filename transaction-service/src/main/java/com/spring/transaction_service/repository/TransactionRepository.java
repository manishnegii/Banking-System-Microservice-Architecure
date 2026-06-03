package com.spring.transaction_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.spring.transaction_service.entity.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    Optional<Transaction> findByGatewayTxnId(String gatewayTxnId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT t FROM Transaction t WHERE t.status = :status ORDER BY t.createdAt DESC")
    List<Transaction> findByStatus(@Param("status") String status);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.merchantId = :merchantId ORDER BY t.createdAt DESC")
    List<Transaction> findByMerchantId(@Param("merchantId") String merchantId);

    @Query("SELECT t FROM Transaction t WHERE t.upiId = :upiId ORDER BY t.createdAt DESC")
    List<Transaction> findByUpiId(@Param("upiId") String upiId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
}

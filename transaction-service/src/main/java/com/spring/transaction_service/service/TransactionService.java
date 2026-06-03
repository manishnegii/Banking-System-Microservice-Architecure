package com.spring.transaction_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.transaction_service.dtos.TransactionRequestDTO;
import com.spring.transaction_service.dtos.TransactionResponseDTO;
import com.spring.transaction_service.dtos.PaymentGatewayLogsDTO;
import com.spring.transaction_service.entity.Transaction;
import com.spring.transaction_service.entity.PaymentGatewayLogs;
import com.spring.transaction_service.exception.TransactionNotFoundException;
import com.spring.transaction_service.exception.InvalidTransactionException;
import com.spring.transaction_service.exception.DuplicateTransactionException;
import com.spring.transaction_service.mapper.TransactionMapper;
import com.spring.transaction_service.repository.TransactionRepository;

import lombok.AllArgsConstructor;

import com.spring.transaction_service.repository.PaymentGatewayLogsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TransactionService {


    private TransactionRepository transactionRepository;
    private PaymentGatewayLogsRepository paymentGatewayLogsRepository;
    private TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO requestDTO) {
        validateTransaction(requestDTO);
        
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(requestDTO.getIdempotencyKey());
        if (existingTransaction.isPresent()) {
            return transactionMapper.toResponseDTO(existingTransaction.get());
        }

        Transaction transaction = transactionMapper.toEntity(requestDTO);
        transaction.setTransactionType("TRANSFER");
        transaction.setStatus("PROCESSING");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponseDTO(savedTransaction);
    }

    // ======================== VIEW TRANSACTION HISTORY ========================
    public List<TransactionResponseDTO> viewTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByUserId(userId);
        return transactions.stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> viewTransactionHistoryByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidTransactionException("Start date must be before end date");
        }
        List<Transaction> transactions = transactionRepository.findTransactionsByUserIdAndDateRange(userId, startDate, endDate);
        return transactions.stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ======================== CHECK TRANSACTION STATUS ========================
    public String checkTransactionStatus(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
        return transaction.getStatus();
    }

    public TransactionResponseDTO checkTransactionDetails(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
        return transactionMapper.toResponseDTO(transaction);
    }

    // ======================== RETRY FAILED TRANSACTION ========================
    @Transactional
    public TransactionResponseDTO retryFailedTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));

        if (!"FAILED".equals(transaction.getStatus())) {
            throw new InvalidTransactionException("Only failed transactions can be retried. Current status: " + transaction.getStatus());
        }

        // Reset transaction for retry
        transaction.setStatus("RETRYING");
        transaction.setFailureReason(null);
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction retriedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponseDTO(retriedTransaction);
    }

    public List<TransactionResponseDTO> getFailedTransactions() {
        List<Transaction> failedTransactions = transactionRepository.findByStatus("FAILED");
        return failedTransactions.stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ======================== MERCHANT PAYMENT ========================
    @Transactional
    public TransactionResponseDTO processMerchantPayment(TransactionRequestDTO requestDTO) {
        // Validate the transaction request
        validateTransaction(requestDTO);

        if (requestDTO.getMerchantId() == null || requestDTO.getMerchantId().isEmpty()) {
            throw new InvalidTransactionException("Merchant ID is required for merchant payments");
        }

        // Check for duplicate using idempotency key
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(requestDTO.getIdempotencyKey());
        if (existingTransaction.isPresent()) {
            return transactionMapper.toResponseDTO(existingTransaction.get());
        }

        // Create merchant payment transaction
        Transaction transaction = transactionMapper.toEntity(requestDTO);
        transaction.setTransactionType("MERCHANT_PAYMENT");
        transaction.setStatus("PROCESSING");
        transaction.setPaymentMethod("MERCHANT_API");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponseDTO(savedTransaction);
    }

    public List<TransactionResponseDTO> getMerchantPayments(String merchantId) {
        List<Transaction> merchantPayments = transactionRepository.findByMerchantId(merchantId);
        return merchantPayments.stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ======================== UPI/QR PAYMENTS ========================
    @Transactional
    public TransactionResponseDTO processUPIQRPayment(TransactionRequestDTO requestDTO) {
        // Validate the transaction request
        validateTransaction(requestDTO);

        if (requestDTO.getUpiId() == null || requestDTO.getUpiId().isEmpty()) {
            throw new InvalidTransactionException("UPI ID is required for UPI/QR payments");
        }

        // Check for duplicate using idempotency key
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(requestDTO.getIdempotencyKey());
        if (existingTransaction.isPresent()) {
            return transactionMapper.toResponseDTO(existingTransaction.get());
        }

        // Create UPI/QR payment transaction
        Transaction transaction = transactionMapper.toEntity(requestDTO);
        transaction.setTransactionType("UPI_QR_PAYMENT");
        transaction.setStatus("PROCESSING");
        transaction.setPaymentMethod(requestDTO.getPaymentMethod() != null ? requestDTO.getPaymentMethod() : "UPI");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponseDTO(savedTransaction);
    }

    public List<TransactionResponseDTO> getUPIPayments(String upiId) {
        List<Transaction> upiPayments = transactionRepository.findByUpiId(upiId);
        return upiPayments.stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ======================== PAYMENT GATEWAY CALLBACK ========================
    @Transactional
    public TransactionResponseDTO handlePaymentGatewayCallback(
            Long transactionId,
            String gatewayTxnId,
            String status,
            String requestPayload,
            String responsePayload,
            String gatewayName) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));

        // Update transaction status based on gateway response
        transaction.setGatewayTxnId(gatewayTxnId);
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());

        // Set failure reason if payment failed
        if ("FAILED".equals(status)) {
            transaction.setFailureReason("Payment gateway returned failure status");
        } else if ("COMPLETED".equals(status) || "SUCCESS".equals(status)) {
            transaction.setStatus("COMPLETED");
            transaction.setFailureReason(null);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);

        // Log gateway response for audit trail
        PaymentGatewayLogs log = PaymentGatewayLogs.builder()
                .txnId(transactionId)
                .gatewayName(gatewayName)
                .requestPayload(requestPayload)
                .responsePayload(responsePayload)
                .gatewayStatus(status)
                .createdAt(LocalDateTime.now())
                .build();

        paymentGatewayLogsRepository.save(log);

        return transactionMapper.toResponseDTO(updatedTransaction);
    }

    public List<PaymentGatewayLogsDTO> getPaymentGatewayLogs(Long transactionId) {
        // Verify transaction exists
        transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));

        List<PaymentGatewayLogs> logs = paymentGatewayLogsRepository.findByTxnId(transactionId);
        return logs.stream()
                .map(transactionMapper::toPaymentGatewayLogsDTO)
                .collect(Collectors.toList());
    }

    // ======================== HELPER METHODS ========================
    private void validateTransaction(TransactionRequestDTO requestDTO) {
        // Validate amount
        if (requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero");
        }

        // Validate user ID
        if (requestDTO.getUserId() == null || requestDTO.getUserId() <= 0) {
            throw new InvalidTransactionException("Invalid user ID");
        }

        // Validate account IDs
        if (requestDTO.getFromAccountId() == null || requestDTO.getFromAccountId() <= 0) {
            throw new InvalidTransactionException("Invalid from account ID");
        }

        if (requestDTO.getToAccountId() == null || requestDTO.getToAccountId() <= 0) {
            throw new InvalidTransactionException("Invalid to account ID");
        }

        // Validate from and to accounts are not the same
        if (requestDTO.getFromAccountId().equals(requestDTO.getToAccountId())) {
            throw new InvalidTransactionException("From and To accounts cannot be the same");
        }

        // Validate transaction type
        if (requestDTO.getTransactionType() == null || requestDTO.getTransactionType().isEmpty()) {
            throw new InvalidTransactionException("Transaction type is required");
        }

        // Validate payment method
        if (requestDTO.getPaymentMethod() == null || requestDTO.getPaymentMethod().isEmpty()) {
            throw new InvalidTransactionException("Payment method is required");
        }

        // Validate idempotency key
        if (requestDTO.getIdempotencyKey() == null || requestDTO.getIdempotencyKey().isEmpty()) {
            throw new InvalidTransactionException("Idempotency key is required");
        }
    }

    @Transactional
    public void updateTransactionStatus(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void updateTransactionStatusWithReason(Long transactionId, String status, String failureReason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
        transaction.setStatus(status);
        transaction.setFailureReason(failureReason);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public long getTransactionCount(Long userId) {
        return transactionRepository.countByUserId(userId);
    }
}

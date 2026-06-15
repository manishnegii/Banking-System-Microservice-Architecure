package com.spring.transaction_service.service;

import com.rexchord.common_security.model.CustomUserPrincipal;
import com.spring.transaction_service.client.AccountClient;
import com.spring.transaction_service.client.UserClient;
import com.spring.transaction_service.client.dto.OperationalRequestDto;
import com.spring.transaction_service.entity.TransactionalStatus;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spring.transaction_service.dtos.TransactionRequestDTO;
import com.spring.transaction_service.dtos.TransactionResponseDTO;
import com.spring.transaction_service.dtos.PaymentGatewayLogsDTO;
import com.spring.transaction_service.entity.Transaction;
import com.spring.transaction_service.entity.PaymentGatewayLogs;
import com.spring.transaction_service.exception.TransactionNotFoundException;
import com.spring.transaction_service.exception.InvalidTransactionException;
import com.spring.transaction_service.mapper.TransactionMapper;
import com.spring.transaction_service.repository.TransactionRepository;

import com.spring.transaction_service.repository.PaymentGatewayLogsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentGatewayLogsRepository paymentGatewayLogsRepository;
    private final TransactionMapper transactionMapper;
    private final AccountClient accountClient;
    private final UserClient userClient;

    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO requestDTO, String idempotencyKey,String userId) {
        validateTransaction(requestDTO);

        boolean debitCompleted = false;
        
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if (existingTransaction.isPresent()) {
            return transactionMapper.toResponseDTO(existingTransaction.get());
        }
        var transaction = createPendingTransaction(idempotencyKey, requestDTO, userId);

        try{
            accountClient.debit(new OperationalRequestDto(transaction.getTransactionId(), requestDTO.getFromAccountNumber(), requestDTO.getAmount()));
            debitCompleted = true;

            accountClient.credit(new OperationalRequestDto(transaction.getTransactionId(), requestDTO.getToAccountNumber(), requestDTO.getAmount()));

            transaction.setStatus(TransactionalStatus.SUCCESS);

        } catch (FeignException e) {
            if(debitCompleted){
                accountClient.refund(new OperationalRequestDto(transaction.getTransactionId(), requestDTO.getFromAccountNumber(), requestDTO.getAmount()));

                transaction.setStatus(TransactionalStatus.COMPENSATED);
            }else{
                transaction.setStatus(TransactionalStatus.FAILED);
            }

        }
        return transactionMapper.toResponseDTO(transaction);
    }

    private Transaction createPendingTransaction(String idempotencyKey,TransactionRequestDTO requestDTO,String userId){

        var authId = Long.valueOf(userId);
        var customerId = userClient.getCustomerId(authId);

        Transaction transaction = transactionMapper.toEntity(requestDTO);
        transaction.setUserId(customerId);
        transaction.setTransactionId(TransactionIdGenerator.generate());
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setTransactionType("TRANSFER");
        transaction.setStatus(TransactionalStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
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
    public TransactionalStatus checkTransactionStatus(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);
        return transaction.getStatus();
    }

    public TransactionResponseDTO checkTransactionDetails(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);
        return transactionMapper.toResponseDTO(transaction);
    }

    // ======================== RETRY FAILED TRANSACTION ========================
    @Transactional
    public TransactionResponseDTO retryFailedTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        if (!TransactionalStatus.FAILED.equals(transaction.getStatus())) {
            throw new InvalidTransactionException("Only failed transactions can be retried. Current status: " + transaction.getStatus());
        }

        // Reset transaction for retry
        transaction.setStatus(TransactionalStatus.RETRYING);
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
//    @Transactional
//    public TransactionResponseDTO processMerchantPayment(TransactionRequestDTO requestDTO) {
//        // Validate the transaction request
//        validateTransaction(requestDTO);
//
//        if (requestDTO.getMerchantId() == null || requestDTO.getMerchantId().isEmpty()) {
//            throw new InvalidTransactionException("Merchant ID is required for merchant payments");
//        }
//
//        // Check for duplicate using idempotency key
//        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(requestDTO.getIdempotencyKey());
//        if (existingTransaction.isPresent()) {
//            return transactionMapper.toResponseDTO(existingTransaction.get());
//        }
//
//        // Create merchant payment transaction
//        Transaction transaction = transactionMapper.toEntity(requestDTO);
//        transaction.setTransactionType("MERCHANT_PAYMENT");
//        transaction.setStatus(TransactionalStatus.PROCESSING);
//        transaction.setPaymentMethod("MERCHANT_API");
//        transaction.setCreatedAt(LocalDateTime.now());
//        transaction.setUpdatedAt(LocalDateTime.now());
//
//        Transaction savedTransaction = transactionRepository.save(transaction);
//        return transactionMapper.toResponseDTO(savedTransaction);
//    }
//
//    public List<TransactionResponseDTO> getMerchantPayments(String merchantId) {
//        List<Transaction> merchantPayments = transactionRepository.findByMerchantId(merchantId);
//        return merchantPayments.stream()
//                .map(transactionMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }

    // ======================== UPI/QR PAYMENTS ========================

//    @Transactional
//    public TransactionResponseDTO processUPIQRPayment(TransactionRequestDTO requestDTO) {
//        // Validate the transaction request
//        validateTransaction(requestDTO);
//
//        if (requestDTO.getUpiId() == null || requestDTO.getUpiId().isEmpty()) {
//            throw new InvalidTransactionException("UPI ID is required for UPI/QR payments");
//        }
//
//        // Check for duplicate using idempotency key
//        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(requestDTO.getIdempotencyKey());
//        if (existingTransaction.isPresent()) {
//            return transactionMapper.toResponseDTO(existingTransaction.get());
//        }
//
//        // Create UPI/QR payment transaction
//        Transaction transaction = transactionMapper.toEntity(requestDTO);
//        transaction.setTransactionType("UPI_QR_PAYMENT");
//        transaction.setStatus(TransactionalStatus.PROCESSING);
//        transaction.setPaymentMethod(requestDTO.getPaymentMethod() != null ? requestDTO.getPaymentMethod() : "UPI");
//        transaction.setCreatedAt(LocalDateTime.now());
//        transaction.setUpdatedAt(LocalDateTime.now());
//
//        Transaction savedTransaction = transactionRepository.save(transaction);
//        return transactionMapper.toResponseDTO(savedTransaction);
//    }
//
//    public List<TransactionResponseDTO> getUPIPayments(String upiId) {
//        List<Transaction> upiPayments = transactionRepository.findByUpiId(upiId);
//        return upiPayments.stream()
//                .map(transactionMapper::toResponseDTO)
//                .collect(Collectors.toList());
//    }

    // ======================== PAYMENT GATEWAY CALLBACK ========================
//    @Transactional
//    public TransactionResponseDTO handlePaymentGatewayCallback(
//            Long transactionId,
//            String gatewayTxnId,
//            TransactionalStatus status,
//            String requestPayload,
//            String responsePayload,
//            String gatewayName) {
//
//        Transaction transaction = transactionRepository.findById(transactionId)
//                .orElseThrow(TransactionFailedException::new);
//
//        // Update transaction status based on gateway response
//        transaction.setGatewayTxnId(gatewayTxnId);
//        transaction.setStatus(status);
//        transaction.setUpdatedAt(LocalDateTime.now());
//
//        // Set failure reason if payment failed
//        if (TransactionalStatus.FAILED.equals(status)) {
//            transaction.setFailureReason("Payment gateway returned failure status");
//        } else if (TransactionalStatus.COMPLETED.equals(status) || TransactionalStatus.SUCCESS.equals(status)) {
//            transaction.setStatus(TransactionalStatus.COMPLETED);
//            transaction.setFailureReason(null);
//        }
//
//        Transaction updatedTransaction = transactionRepository.save(transaction);
//
//        // Log gateway response for audit trail
//        PaymentGatewayLogs log = PaymentGatewayLogs.builder()
//                .txnId(transactionId)
//                .gatewayName(gatewayName)
//                .requestPayload(requestPayload)
//                .responsePayload(responsePayload)
//                .gatewayStatus(status)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        paymentGatewayLogsRepository.save(log);
//
//        return transactionMapper.toResponseDTO(updatedTransaction);
//    }

    public List<PaymentGatewayLogsDTO> getPaymentGatewayLogs(Long transactionId) {
        // Verify transaction exists
        transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

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
    }

    @Transactional
    public void updateTransactionStatus(Long transactionId, TransactionalStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void updateTransactionStatusWithReason(Long transactionId, TransactionalStatus status, String failureReason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);
        transaction.setStatus(status);
        transaction.setFailureReason(failureReason);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public long getTransactionCount(Long userId) {
        return transactionRepository.countByUserId(userId);
    }
}

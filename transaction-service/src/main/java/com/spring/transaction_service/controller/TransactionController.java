package com.spring.transaction_service.controller;

import com.rexchord.common_security.model.CustomUserPrincipal;
import com.spring.transaction_service.entity.TransactionalStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import com.spring.transaction_service.dtos.TransactionRequestDTO;
import com.spring.transaction_service.dtos.TransactionResponseDTO;
import com.spring.transaction_service.dtos.PaymentGatewayLogsDTO;
import com.spring.transaction_service.dtos.ApiResponse;
import com.spring.transaction_service.service.TransactionService;
import com.spring.transaction_service.exception.TransactionNotFoundException;
import com.spring.transaction_service.exception.InvalidTransactionException;
import com.spring.transaction_service.exception.DuplicateTransactionException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
//@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;


    @GetMapping("/debug")
    public Object debug(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> transferMoney(
            @Valid @RequestBody TransactionRequestDTO requestDTO, @RequestHeader("Idempotency-Key") String idempotencyKey, @AuthenticationPrincipal CustomUserPrincipal user) {
        try {
            TransactionResponseDTO response = transactionService.transferMoney(requestDTO,idempotencyKey,user.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Money transfer initiated successfully", response));
        } catch (DuplicateTransactionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (InvalidTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error processing transfer: " + e.getMessage(), "TRANSFER_ERROR"));
        }
    }

    // ======================== VIEW TRANSACTION HISTORY ========================
    @GetMapping("/view-history/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> viewTransactionHistory(
            @PathVariable Long userId) {
        try {
            List<TransactionResponseDTO> response = transactionService.viewTransactionHistory(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction history retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving transaction history", "HISTORY_ERROR"));
        }
    }

    @GetMapping("/view-history/{userId}/range")
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> viewTransactionHistoryByRange(
            @PathVariable Long userId,
            @RequestParam(required = true) LocalDateTime startDate,
            @RequestParam(required = true) LocalDateTime endDate) {
        try {
            List<TransactionResponseDTO> response = transactionService.viewTransactionHistoryByDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction history retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving transaction history", "HISTORY_RANGE_ERROR"));
        }
    }

//    @GetMapping("/view-history/merchant/{merchantId}")
//    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> viewMerchantTransactionHistory(
//            @PathVariable String merchantId) {
//        try {
//            List<TransactionResponseDTO> response = transactionService.getMerchantPayments(merchantId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "Merchant transaction history retrieved successfully", response));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error retrieving merchant history", "MERCHANT_HISTORY_ERROR"));
//        }
//    }

    // ======================== CHECK STATUS ========================
    @GetMapping("/check-status/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionalStatus>> checkStatus(@PathVariable Long transactionId) {
        try {
            var status = transactionService.checkTransactionStatus(transactionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction status retrieved successfully", status));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error checking status", "STATUS_CHECK_ERROR"));
        }
    }

    @GetMapping("/check-status-details/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> checkStatusDetails(@PathVariable Long transactionId) {
        try {
            TransactionResponseDTO response = transactionService.checkTransactionDetails(transactionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction details retrieved successfully", response));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving transaction details", "DETAILS_ERROR"));
        }
    }

    // ======================== RETRY FAILED ========================
    @PostMapping("/retry-failed/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> retryFailed(@PathVariable Long transactionId) {
        try {
            TransactionResponseDTO response = transactionService.retryFailedTransaction(transactionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transaction retry initiated successfully", response));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (InvalidTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrying transaction", "RETRY_ERROR"));
        }
    }

    @GetMapping("/get-failed-transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getFailedTransactions() {
        try {
            List<TransactionResponseDTO> response = transactionService.getFailedTransactions();
            return ResponseEntity.ok(new ApiResponse<>(true, "Failed transactions retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving failed transactions", "FAILED_TXN_ERROR"));
        }
    }

//    // ======================== MERCHANT PAYMENT ========================
//    @PostMapping("/merchant-payment")
//    public ResponseEntity<ApiResponse<TransactionResponseDTO>> merchantPayment(
//            @Valid @RequestBody TransactionRequestDTO requestDTO) {
//        try {
//            TransactionResponseDTO response = transactionService.processMerchantPayment(requestDTO);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse<>(true, "Merchant payment processed successfully", response));
//        } catch (InvalidTransactionException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
//        } catch (DuplicateTransactionException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error processing merchant payment", "MERCHANT_PAYMENT_ERROR"));
//        }
//    }

//    @GetMapping("/merchant-payment/{merchantId}")
//    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getMerchantPayments(
//            @PathVariable String merchantId) {
//        try {
//            List<TransactionResponseDTO> response = transactionService.getMerchantPayments(merchantId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "Merchant payments retrieved successfully", response));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error retrieving merchant payments", "MERCHANT_PAYMENTS_ERROR"));
//        }
//    }
//
//    // ======================== UPI/QR PAYMENTS ========================
//    @PostMapping("/upi-qr-payment")
//    public ResponseEntity<ApiResponse<TransactionResponseDTO>> upiQrPayment(
//            @Valid @RequestBody TransactionRequestDTO requestDTO) {
//        try {
//            TransactionResponseDTO response = transactionService.processUPIQRPayment(requestDTO);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse<>(true, "UPI/QR payment processed successfully", response));
//        } catch (InvalidTransactionException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
//        } catch (DuplicateTransactionException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error processing UPI/QR payment", "UPI_QR_ERROR"));
//        }
//    }
//
//    @GetMapping("/upi-qr-payment/{upiId}")
//    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getUPIQRPayments(
//            @PathVariable String upiId) {
//        try {
//            List<TransactionResponseDTO> response = transactionService.getUPIPayments(upiId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "UPI/QR payments retrieved successfully", response));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error retrieving UPI/QR payments", "UPI_QR_RETRIEVE_ERROR"));
//        }
//    }
//
//    // ======================== PAYMENT GATEWAY CALLBACK ========================
//    @PostMapping("/payments-callback/{transactionId}")
//    public ResponseEntity<ApiResponse<TransactionResponseDTO>> paymentsCallback(
//            @PathVariable Long transactionId,
//            @RequestParam String gatewayTxnId,
//            @RequestParam String status,
//            @RequestParam String gatewayName,
//            @RequestParam(required = false) String requestPayload,
//            @RequestParam(required = false) String responsePayload) {
//        try {
//            TransactionResponseDTO response = transactionService.handlePaymentGatewayCallback(
//                    transactionId, gatewayTxnId, status, requestPayload, responsePayload, gatewayName);
//            return ResponseEntity.ok(new ApiResponse<>(true, "Payment gateway callback processed successfully", response));
//        } catch (TransactionNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, e.getMessage(), e.getErrorCode()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error processing payment callback", "CALLBACK_ERROR"));
//        }
//    }

    @GetMapping("/payments-callback-logs/{transactionId}")
    public ResponseEntity<ApiResponse<List<PaymentGatewayLogsDTO>>> getPaymentCallbackLogs(@PathVariable Long transactionId) {
        try {
            List<PaymentGatewayLogsDTO> response = transactionService.getPaymentGatewayLogs(transactionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Payment callback logs retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving payment callback logs", "CALLBACK_LOGS_ERROR"));
        }
    }
}

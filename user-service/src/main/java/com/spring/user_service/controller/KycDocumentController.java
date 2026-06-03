package com.spring.user_service.controller;

import com.spring.user_service.dto.KycDocumentCreateRequestDto;
import com.spring.user_service.dto.KycDocumentResponseDto;
import com.spring.user_service.dto.KycDocumentUpdateRequestDto;
import com.spring.user_service.dto.KycStatusResponse;
import com.spring.user_service.entity.DocumentType;
import com.spring.user_service.entity.KycStatus;
import com.spring.user_service.service.KycDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers-docs")
@RequiredArgsConstructor
public class KycDocumentController {

    private final KycDocumentService kycDocumentService;

    @PostMapping("/{customerId}/kyc-document")
    public ResponseEntity<KycDocumentResponseDto> uploadKycDocument(@PathVariable Long customerId,
            @Valid @RequestBody KycDocumentCreateRequestDto requestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        KycDocumentResponseDto response = kycDocumentService.uploadKycDocument(customerId,requestDto);
        URI location = uriComponentsBuilder.path("/api/v1/kyc-documents/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<KycDocumentResponseDto> getKycDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentById(documentId));
    }

    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<KycDocumentResponseDto> getKycDocumentByNumber(
            @PathVariable String documentNumber) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentByNumber(documentNumber));
    }

    @GetMapping("/kyc-document/customer")
    public ResponseEntity<List<KycDocumentResponseDto>> getKycDocumentsByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentsByCustomerId(customerId));
    }

    @GetMapping("/{customerId}/kyc-status")
    public ResponseEntity<KycStatusResponse> getStatus(@PathVariable Long customerId){
        return ResponseEntity.ok(kycDocumentService.getKycStatus(customerId));
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<KycDocumentResponseDto>> getKycDocumentsByCustomerAndStatus(
            @PathVariable Long customerId,
            @PathVariable KycStatus status) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentsByCustomerIdAndStatus(customerId, status));
    }

    @GetMapping("/kyc-document/type/{type}")
    public ResponseEntity<List<KycDocumentResponseDto>> getKycDocumentsByCustomerAndType(
            @PathVariable Long customerId,
            @PathVariable DocumentType type) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentsByCustomerIdAndType(customerId, type));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<KycDocumentResponseDto> updateKycDocument(
            @PathVariable Long documentId,
            @Valid @RequestBody KycDocumentUpdateRequestDto requestDto) {
        return ResponseEntity.ok(kycDocumentService.updateKycDocument(documentId, requestDto));
    }

    @PostMapping("/{documentId}/approve")
    public ResponseEntity<KycDocumentResponseDto> approveKycDocument(
            @PathVariable Long documentId,
            @RequestParam(required = false) LocalDateTime verifiedAt) {
        return ResponseEntity.ok(kycDocumentService.approveKycDocument(documentId, verifiedAt));
    }

    @PostMapping("/{documentId}/reject")
    public ResponseEntity<KycDocumentResponseDto> rejectKycDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(kycDocumentService.rejectKycDocument(documentId));
    }

    @PostMapping("/{documentId}/review")
    public ResponseEntity<KycDocumentResponseDto> moveToReview(@PathVariable Long documentId) {
        return ResponseEntity.ok(kycDocumentService.moveToReview(documentId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteKycDocument(@PathVariable Long documentId) {
        kycDocumentService.deleteKycDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/count")
    public ResponseEntity<Long> getKycDocumentCount(@PathVariable Long customerId) {
        return ResponseEntity.ok(kycDocumentService.getKycDocumentCountByCustomerId(customerId));
    }

    @GetMapping("/customer/pending-count")
    public ResponseEntity<Long> getPendingKycDocumentCount(@PathVariable Long customerId) {
        return ResponseEntity.ok(kycDocumentService.getPendingKycDocumentCount(customerId));
    }
}

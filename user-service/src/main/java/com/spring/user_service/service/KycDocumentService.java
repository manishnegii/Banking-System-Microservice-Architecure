package com.spring.user_service.service;

import com.spring.user_service.dto.KycDocumentCreateRequestDto;
import com.spring.user_service.dto.KycDocumentResponseDto;
import com.spring.user_service.dto.KycDocumentUpdateRequestDto;
import com.spring.user_service.dto.KycStatusResponse;
import com.spring.user_service.encryption.AesEncryptionService;
import com.spring.user_service.encryption.KeyManagerService;
import com.spring.user_service.encryption.hash.Sha256Utils;
import com.spring.user_service.entity.DocumentType;
import com.spring.user_service.entity.KycDocument;
import com.spring.user_service.entity.KycStatus;
import com.spring.user_service.exception.CustomerNotFoundException;
import com.spring.user_service.exception.DuplicateKycDocumentException;
import com.spring.user_service.exception.KycDocumentNotFoundException;
import com.spring.user_service.mapper.KycDocumentMapper;
import com.spring.user_service.repository.CustomerRepository;
import com.spring.user_service.repository.KycDocumentRepository;
import com.spring.user_service.utility.MaskingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KycDocumentService {

    private final KycDocumentRepository kycDocumentRepository;
    private final CustomerRepository customerRepository;
    private final KycDocumentMapper kycDocumentMapper;
    private final AesEncryptionService encryptionService;
    private final KeyManagerService keyManagerService;

    public KycDocumentResponseDto uploadKycDocument(Long id,KycDocumentCreateRequestDto requestDto) {
        // Validate customer exists
        var customerId = customerRepository.findById(id)
            .orElseThrow(CustomerNotFoundException::new);

        var hash = Sha256Utils.hash(requestDto.getDocumentNumber());
        if (kycDocumentRepository.existsByDocumentNumber(hash)) {
            throw new DuplicateKycDocumentException();
        }

        var kycDocument = kycDocumentMapper.toEntity(requestDto);

        //encrypted
        var keyVersion = keyManagerService.getActiveVersion();

        var encryptedDocument = encryptionService.encrypt(requestDto.getDocumentNumber(),keyVersion);

        kycDocument.setDocumentNumber(encryptedDocument);
        kycDocument.setDocumentHash(hash);
        kycDocument.setKeyVersion(keyVersion);
        kycDocument.setCustomer(customerId);
        var saved = kycDocumentRepository.save(kycDocument);

        //decrypt
        var response = kycDocumentMapper.toResponseDto(saved);
        var decrypted = encryptionService.decrypt(saved.getDocumentNumber(), saved.getKeyVersion());
        response.setMaskedDocumentNumber(MaskingUtils.maskDocument(decrypted,saved.getDocumentType()));
        return response;
    }



    public KycDocumentResponseDto getKycDocumentById(Long id) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);
        return kycDocumentMapper.toResponseDto(kycDocument);
    }

    public KycDocumentResponseDto getKycDocumentByNumber(String documentNumber) {
        KycDocument kycDocument = kycDocumentRepository.findByDocumentNumber(documentNumber)
            .orElseThrow(KycDocumentNotFoundException::new);
        return kycDocumentMapper.toResponseDto(kycDocument);
    }

    public List<KycDocumentResponseDto> getKycDocumentsByCustomerId(Long customerId) {
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(CustomerNotFoundException::new);

        return kycDocumentRepository.findByCustomerId(customerId).stream()
            .map(kycDocumentMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public List<KycDocumentResponseDto> getKycDocumentsByCustomerIdAndStatus(Long customerId, KycStatus status) {
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(CustomerNotFoundException::new);

        return kycDocumentRepository.findByCustomerIdAndVerificationStatus(customerId, status).stream()
            .map(kycDocumentMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public List<KycDocumentResponseDto> getKycDocumentsByCustomerIdAndType(Long customerId, DocumentType type) {
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(CustomerNotFoundException::new);

        return kycDocumentRepository.findByCustomerIdAndDocumentType(customerId, type).stream()
            .map(kycDocumentMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public KycDocumentResponseDto updateKycDocument(Long id, KycDocumentUpdateRequestDto requestDto) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);

        kycDocumentMapper.updateEntityFromDto(requestDto, kycDocument);
        KycDocument updated = kycDocumentRepository.save(kycDocument);
        return kycDocumentMapper.toResponseDto(updated);
    }

    public KycDocumentResponseDto approveKycDocument(Long id, LocalDateTime verifiedAt) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);

        kycDocument.setVerificationStatus(KycStatus.VERIFIED);
        kycDocument.setVerifiedAt(verifiedAt != null ? verifiedAt : LocalDateTime.now());

        KycDocument updated = kycDocumentRepository.save(kycDocument);
        return kycDocumentMapper.toResponseDto(updated);
    }

    public KycDocumentResponseDto rejectKycDocument(Long id) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);

        kycDocument.setVerificationStatus(KycStatus.REJECTED);
        KycDocument updated = kycDocumentRepository.save(kycDocument);
        return kycDocumentMapper.toResponseDto(updated);
    }

    public KycDocumentResponseDto moveToReview(Long id) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);

        kycDocument.setVerificationStatus(KycStatus.IN_REVIEW);
        KycDocument updated = kycDocumentRepository.save(kycDocument);
        return kycDocumentMapper.toResponseDto(updated);
    }

    public KycStatusResponse getKycStatus(Long customerId){
        var kycDocument = kycDocumentRepository.findLatestByCustomerId(customerId)
                .orElseThrow(KycDocumentNotFoundException::new);
        return kycDocumentMapper.statusResponseDto(kycDocument);
    }

    public void deleteKycDocument(Long id) {
        KycDocument kycDocument = kycDocumentRepository.findById(id)
            .orElseThrow(KycDocumentNotFoundException::new);
        kycDocumentRepository.deleteById(id);
    }

    public void deleteKycDocumentsByCustomerId(Long customerId) {
        kycDocumentRepository.deleteByCustomerId(customerId);
    }

    public long getKycDocumentCountByCustomerId(Long customerId) {
        return kycDocumentRepository.countByCustomerId(customerId);
    }

    public long getPendingKycDocumentCount(Long customerId) {
        return kycDocumentRepository.countByCustomerIdAndVerificationStatus(customerId, KycStatus.PENDING);
    }
}

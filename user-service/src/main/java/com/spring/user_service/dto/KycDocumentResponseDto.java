package com.spring.user_service.dto;

import com.spring.user_service.entity.DocumentType;
import com.spring.user_service.entity.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycDocumentResponseDto {
    private Long id;
    private Long customerId;
    private DocumentType documentType;
    private String maskedDocumentNumber;
    private String documentUrl;
    private KycStatus verificationStatus;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
}

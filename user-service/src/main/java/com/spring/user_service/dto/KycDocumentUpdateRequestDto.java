package com.spring.user_service.dto;

import com.spring.user_service.entity.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KycDocumentUpdateRequestDto {

    private String documentUrl;

    private KycStatus verificationStatus;

    private LocalDateTime verifiedAt;

    private LocalDateTime expiryDate;
}

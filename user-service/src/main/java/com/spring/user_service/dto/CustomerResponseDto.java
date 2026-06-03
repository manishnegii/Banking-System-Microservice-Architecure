package com.spring.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private Long id;
    private Long authId;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String mobileNumber;
    private List<AddressResponseDto> addresses;
    private List<KycDocumentResponseDto> kycDocuments;
}

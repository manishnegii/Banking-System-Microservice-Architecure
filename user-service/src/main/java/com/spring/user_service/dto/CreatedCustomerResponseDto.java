package com.spring.user_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedCustomerResponseDto {
    private Long id;
    private Long authId;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String mobileNumber;
}

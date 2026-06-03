package com.spring.user_service.dto;

import com.spring.user_service.entity.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KycStatusResponse {
    private Long customerId;
    private KycStatus kycStatus;
}

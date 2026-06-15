package com.spring.account_service.client;

import com.spring.account_service.client.dto.CustomerResponse;
import com.spring.account_service.dto.OperationalRequestDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/customers/auth/{authId}/customer-id")
    Long getCustomerId(@PathVariable Long authId);

    @GetMapping("/api/v1/customers-docs/{customerId}/kyc-status")
    CustomerResponse getStatus(@PathVariable Long customerId);
}

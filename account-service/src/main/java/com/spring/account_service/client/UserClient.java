package com.spring.account_service.client;

import com.spring.account_service.client.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/customers-docs/{customerId}/kyc-status")
    CustomerResponse getStatus(@PathVariable Long customerId);
}

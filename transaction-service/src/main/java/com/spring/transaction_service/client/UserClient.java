package com.spring.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/customers/auth/{authId}/customer-id")
    Long getCustomerId(@PathVariable Long authId);
}

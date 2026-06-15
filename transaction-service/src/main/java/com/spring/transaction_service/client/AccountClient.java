package com.spring.transaction_service.client;

import com.spring.transaction_service.client.dto.AccountRequestDto;
import com.spring.transaction_service.client.dto.OperationalRequestDto;
import com.spring.transaction_service.client.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", configuration = FeignConfig.class)
public interface AccountClient {

    @PostMapping("api/v1/accounts/accountNumber")
    ResponseDto getAccountDetails(@RequestBody AccountRequestDto requestDto);

    @PostMapping("/debit")
    Void debit(@Valid @RequestBody OperationalRequestDto request);

    @PostMapping("/credit")
    Void credit(@Valid @RequestBody OperationalRequestDto request);

    @PostMapping("/refund")
    Void refund(@Valid @RequestBody OperationalRequestDto request);
}

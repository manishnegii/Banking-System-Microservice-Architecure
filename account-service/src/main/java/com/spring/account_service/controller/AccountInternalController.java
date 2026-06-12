package com.spring.account_service.controller;

import com.spring.account_service.dto.OperationalRequestDto;
import com.spring.account_service.service.AccountInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/accounts")
@RequiredArgsConstructor
public class AccountInternalController {

    private final AccountInternalService internalService;

    @PostMapping("/debit")
    public ResponseEntity<Void> debit(@Valid @RequestBody OperationalRequestDto request){
        internalService.debit(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credit")
    public ResponseEntity<Void> credit(@Valid @RequestBody OperationalRequestDto request){
        internalService.credit(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> refund(@Valid @RequestBody OperationalRequestDto request){
        internalService.refund(request);
        return ResponseEntity.ok().build();
    }
}

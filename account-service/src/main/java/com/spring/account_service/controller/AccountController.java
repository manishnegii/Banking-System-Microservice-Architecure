package com.spring.account_service.controller;

import com.spring.account_service.dto.*;
import com.spring.account_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/debug")
    public Object debug(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @PostMapping("/open")
    public ResponseEntity<AccountResponseDto> openAccount(@Valid @RequestBody CreateAccountRequestDto requestDto,
                                                             UriComponentsBuilder uriComponentsBuilder) {
        AccountResponseDto response = accountService.openAccount(requestDto);
        URI location = uriComponentsBuilder.path("/api/v1/accounts/{id}").buildAndExpand(response.getAccountId()).toUri();
        return ResponseEntity.created(location).body(response);
    }



    @PostMapping("/accountNumber")
    public ResponseEntity<AccountResponseDto> getAccountDetails(@Valid @RequestBody AccountNumberRequestDto requestDto) {
        return ResponseEntity.ok(accountService.getAccountDetails(requestDto));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponseDto> checkBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.checkBalance(accountId));
    }

    @GetMapping("/{accountId}/statement")
    public ResponseEntity<byte[]> downloadStatement(
        @PathVariable Long accountId,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate from,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate to
    ) {
        byte[] statementBytes = accountService.downloadStatement(accountId, from, to);

        String fileName = "statement_" + accountId + ".csv";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .contentType(MediaType.valueOf("text/csv"))
            .body(statementBytes);
    }

//    @PutMapping("/{accountId}")
//    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable Long accountId,
//                                                              @Valid @RequestBody UpdateAccountRequestDto requestDto) {
//        return ResponseEntity.ok(accountService.updateAccount(accountId, requestDto));
//    }

    @PostMapping("/{accountId}/freeze")
    public ResponseEntity<AccountResponseDto> freezeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.freezeAccount(accountId));
    }

    @PostMapping("/{accountId}/unfreeze")
    public ResponseEntity<AccountResponseDto> unfreezeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.unfreezeAccount(accountId));
    }

    @PostMapping("/{accountId}/close")
    public ResponseEntity<AccountResponseDto> closeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.closeAccount(accountId));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}


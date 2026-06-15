package com.spring.account_service.service;

import com.rexchord.common_security.model.CustomUserPrincipal;
import com.spring.account_service.client.UserClient;
import com.spring.account_service.client.dto.CustomerResponse;
import com.spring.account_service.dto.AccountNumberRequestDto;
import com.spring.account_service.dto.AccountResponseDto;
import com.spring.account_service.dto.BalanceResponseDto;
import com.spring.account_service.dto.CreateAccountRequestDto;
import com.spring.account_service.entity.Account;
import com.spring.account_service.entity.AccountStatus;
import com.spring.account_service.entity.AccountType;
import com.spring.account_service.exception.*;
import com.spring.account_service.mapper.AccountMapper;
import com.spring.account_service.repository.AccountRepository;
import com.spring.account_service.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountNumberGenerator numberGenerator;
    private final BranchRepository branchRepository;
    private final UserClient userClient;

    public AccountResponseDto openAccount(CreateAccountRequestDto requestDto) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (CustomUserPrincipal)authentication.getPrincipal();
        var authId = Long.valueOf(user.getUserId());
        var customerId = userClient.getCustomerId(authId);

        var response = userClient.getStatus(customerId);

        if(!"VERIFIED".equalsIgnoreCase(response.getKycStatus())){
                    throw new KycException();
        }

        var accountExists = accountRepository.existsByUserIdAndAccountType(response.getCustomerId(),requestDto.getAccountType());
        var branch = branchRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Branch Not found"));

        if(accountExists){
            throw new AccountAlreadyExists();
        }
        var account = accountMapper.toEntity(requestDto);
        account.setUserId(customerId);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBranch(branch);
        account.setAccountNumber(numberGenerator.generate(branch,requestDto.getAccountType()));
        normalizeBalance(account);

        var saved = accountRepository.save(account);
        return accountMapper.toResponseDto(saved);
    }



    public AccountResponseDto getAccountDetails(AccountNumberRequestDto requestDto) {
        var account = accountRepository.findByAccountNumber(requestDto.getAccountNumber());
        return accountMapper.toResponseDto(account);
    }


    public BalanceResponseDto checkBalance(Long accountId) {
        var account = requireAccount(accountId);
        return accountMapper.toBalanceResponseDto(account);
    }


    public byte[] downloadStatement(Long accountId, LocalDate from, LocalDate to) {
        Account account = requireAccount(accountId);

        // Minimal statement stub (no transactions table in this repo yet).
        LocalDate effectiveFrom = from != null ? from : LocalDate.now().minusDays(30);
        LocalDate effectiveTo = to != null ? to : LocalDate.now();

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        String sb = "account_id,account_number,user_id,period_start,period_end,balance,currency,status\n" +
                account.getAccountId() + ',' +
                escapeCsv(account.getAccountNumber()) + ',' +
                account.getUserId() + ',' +
                effectiveFrom.format(fmt) + ',' +
                effectiveTo.format(fmt) + ',' +
                account.getBalance() + ',' +
                account.getCurrency() + ',' +
                account.getStatus() + '\n';

        return sb.getBytes(StandardCharsets.UTF_8);
    }

//    public AccountResponseDto updateAccount(Long accountId, UpdateAccountRequestDto requestDto) {
//        Account account = requireAccount(accountId);
//        ensureNotClosed(account);
//
//        // Account number is unique, so enforce it when changing.
//        if (!account.getAccountNumber().equals(requestDto.getAccountNumber())
//            && accountRepository.existsByAccountNumber(requestDto.getAccountNumber())) {
//            throw new DuplicateAccountException();
//        }
//
//        accountMapper.updateEntityFromDto(requestDto, account);
//        normalizeBalance(account);
//
//        Account saved = accountRepository.save(account);
//        return accountMapper.toResponseDto(saved);
//    }

    public AccountResponseDto freezeAccount(Long accountId) {
        Account account = requireAccount(accountId);

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new AccountStateConflictException("Cannot freeze a closed account");
        }

        if (account.getStatus() != AccountStatus.FROZEN) {
            account.setStatus(AccountStatus.FROZEN);
        }

        Account saved = accountRepository.save(account);
        return accountMapper.toResponseDto(saved);
    }


    public AccountResponseDto unfreezeAccount(Long accountId) {
        Account account = requireAccount(accountId);

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new AccountStateConflictException("Cannot unfreeze a closed account");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            account.setStatus(AccountStatus.ACTIVE);
        }

        Account saved = accountRepository.save(account);
        return accountMapper.toResponseDto(saved);
    }


    public AccountResponseDto closeAccount(Long accountId) {
        Account account = requireAccount(accountId);

        if (account.getStatus() != AccountStatus.CLOSED) {
            account.setStatus(AccountStatus.CLOSED);
        }

        Account saved = accountRepository.save(account);
        return accountMapper.toResponseDto(saved);
    }

    private Account requireAccount(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidAccountIdException();
        }
        return accountRepository.findById(accountId)
            .orElseThrow(AccountNotFoundException::new);
    }

    private void ensureNotClosed(Account account) {
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new ClosedAccountOperationException();
        }
    }

    private void normalizeBalance(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
            return;
        }
        account.setBalance(account.getBalance().setScale(2, RoundingMode.UNNECESSARY));
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // Naive CSV escaping (enough for account_number/currency fields).
        String v = value.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n") || v.contains("\r") || v.contains("\"")) {
            return "\"" + v + "\"";
        }
        return v;
    }
}


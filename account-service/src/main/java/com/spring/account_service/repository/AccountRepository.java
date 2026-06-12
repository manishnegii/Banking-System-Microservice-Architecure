package com.spring.account_service.repository;

import com.spring.account_service.entity.Account;
import com.spring.account_service.entity.AccountType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserIdAndAccountType(Long userId,AccountType accountType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountNumber = :accountNumber")
    Account findByAccountNumber(String accountNumber);

    @Modifying
    @Query("update Account a set a.balance = a.balance -:amount where a.accountNumber = :accountNumber and a.balance >= :amount")
    int debitBalance(Long accountNumber, BigDecimal amount);

    @Modifying
    @Query("update Account a set a.balance = a.balance +:amount where a.accountNumber = :accountNumber")
    int creditBalance(Long accountNumber, BigDecimal amount);



    List<Account> findByUserId(Long userId);
}


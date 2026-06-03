package com.spring.account_service.repository;

import com.spring.account_service.entity.Account;
import com.spring.account_service.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUserIdAndAccountType(Long userId,AccountType accountType);

    List<Account> findByUserId(Long userId);
}


package com.spring.account_service.repository;

import com.spring.account_service.entity.AccountNumberSequence;
import com.spring.account_service.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountNumberSequenceRepository extends JpaRepository<AccountNumberSequence,Long> {

    Optional<AccountNumberSequence> findByBranchCodeAndAccountType(String branchCode, AccountType accountType);
}

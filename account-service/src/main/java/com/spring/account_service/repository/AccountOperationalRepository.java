package com.spring.account_service.repository;

import com.spring.account_service.entity.AccountOperations;
import com.spring.account_service.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountOperationalRepository extends JpaRepository<AccountOperations, Long> {

    @Modifying
    @Query("update AccountOperations o set o.status = 'SUCCESS' where o.txnId = :txnId and o.operationType = :operationType")
    void markSuccess(String txnId,OperationType operationType);
}

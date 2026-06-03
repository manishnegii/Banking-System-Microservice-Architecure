package com.spring.account_service.repository;

import com.spring.account_service.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch,Long> {
    Optional<Branch> findByBranchCode(String branchCode);

}

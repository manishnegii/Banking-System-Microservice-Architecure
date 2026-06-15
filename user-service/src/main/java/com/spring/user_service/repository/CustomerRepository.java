package com.spring.user_service.repository;

import com.spring.user_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c.id FROM Customer c WHERE c.authId=:authId")
    Long findCustomerIdByAuthId(@Param("authId") Long AuthId);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<Customer> findByAuthId(Long authId);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByMobileNumber(String mobileNumber);

    List<Customer> findByFullNameContainingIgnoreCase(String fullName);

//    long countByEmailVerified();
}

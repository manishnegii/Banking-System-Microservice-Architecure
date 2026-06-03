package com.spring.user_service.repository;

import com.spring.user_service.entity.Address;
import com.spring.user_service.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomerId(Long customerId);

    List<Address> findByCustomerIdAndAddressType(Long customerId, AddressType addressType);

    Optional<Address> findByCustomerIdAndIsPrimaryTrue(Long customerId);

    long countByCustomerId(Long customerId);

    boolean existsByCustomerIdAndIsPrimaryTrue(Long customerId);

    void deleteByCustomerId(Long customerId);
}

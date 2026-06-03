package com.spring.user_service.repository;

import com.spring.user_service.entity.DocumentType;
import com.spring.user_service.entity.KycDocument;
import com.spring.user_service.entity.KycStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {

    @Query("SELECT k FROM KycDocument k WHERE k.customer.id = :customerId ORDER BY k.createdAt DESC")
    Optional<KycDocument> findLatestByCustomerId(@Param("customerId") Long customerId);

    List<KycDocument> findByCustomerId(Long customerId);

    List<KycDocument> findByCustomerIdAndVerificationStatus(Long customerId, KycStatus verificationStatus);

    List<KycDocument> findByCustomerIdAndDocumentType(Long customerId, DocumentType documentType);

    Optional<KycDocument> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByCustomerIdAndDocumentType(Long customerId, DocumentType documentType);

    long countByCustomerId(Long customerId);

    long countByCustomerIdAndVerificationStatus(Long customerId, KycStatus verificationStatus);

    void deleteByCustomerId(Long customerId);
}

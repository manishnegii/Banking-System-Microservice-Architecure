package com.spring.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers",
        indexes = {
        @Index(name = "idx_customer_auth_id",columnList = "auth_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "auth_id", nullable = false, unique = true)
    private Long authId;

    @Column(name = "name", nullable = false)
    private String fullName;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setCustomer(null);
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KycDocument> kycDocuments = new ArrayList<>();

    public void addKycDocument(KycDocument kycDocument) {
        kycDocuments.add(kycDocument);
        kycDocument.setCustomer(this);
    }

    public void removeKycDocument(KycDocument kycDocument) {
        kycDocuments.remove(kycDocument);
        kycDocument.setCustomer(null);
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();

    }
}




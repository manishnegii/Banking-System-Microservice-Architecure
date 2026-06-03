package com.spring.account_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_numberSequence", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"branchCode","accountType"})
})
@Getter
@Setter
public class AccountNumberSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "branch_code")
    private String branchCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name ="next_value")
    private Long nextValue;

    @Column(name = "max_value")
    private Long maxValue;

    @Column(name = "active")
    private boolean active;

    @Version
    @Column(name = "version")
    private Long version;
}

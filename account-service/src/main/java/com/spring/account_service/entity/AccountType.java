package com.spring.account_service.entity;


import lombok.Getter;

@Getter
public enum AccountType {
    SAVINGS("10"),
    CURRENT("11"),
    FIXED_DEPOSIT("13"),
    RECURRING_DEPOSIT("14"),
    LOAN("20");

    private final String code;

    private AccountType(String code){
        this.code = code;
    }

}

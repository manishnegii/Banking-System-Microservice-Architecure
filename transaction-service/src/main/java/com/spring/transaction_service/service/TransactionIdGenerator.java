package com.spring.transaction_service.service;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class TransactionIdGenerator {
    public static String generate(){
        return "TXN-" + UUID.randomUUID().toString().replace("-","").substring(0,16).toUpperCase();
    }
}

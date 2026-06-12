package com.spring.account_service.service;

import com.spring.account_service.dto.OperationalRequestDto;
import com.spring.account_service.entity.OperationType;
import com.spring.account_service.exception.BalanceException;
import com.spring.account_service.mapper.AccountMapper;
import com.spring.account_service.repository.AccountOperationalRepository;
import com.spring.account_service.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountInternalService {

    private final AccountOperationalRepository operationalRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public void debit(OperationalRequestDto requestDto){

        try {
            var operation = accountMapper.operationalEntity(requestDto);
            operation.setTxnId(requestDto.getTxnId());
            operation.setOperationType(OperationType.DEBIT);
            operation.setAmount(requestDto.getAmount());

            operationalRepository.save(operation);
        }catch (DataIntegrityViolationException e){
                return;
        }

        int updated = accountRepository.debitBalance(requestDto.getAccountNumber(), requestDto.getAmount());

        if(updated == 0){
            throw new BalanceException();
        }
        operationalRepository.markSuccess(requestDto.getTxnId(),OperationType.DEBIT);
    }

    @Transactional
    public void credit(OperationalRequestDto requestDto){

        try {
            var operation = accountMapper.operationalEntity(requestDto);
            operation.setTxnId(requestDto.getTxnId());
            operation.setOperationType(OperationType.CREDIT);
            operation.setAmount(requestDto.getAmount());

            operationalRepository.save(operation);
        }catch (DataIntegrityViolationException e){
            return;
        }

        int updated = accountRepository.creditBalance(requestDto.getAccountNumber(), requestDto.getAmount());

        if(updated == 0){
            throw new BalanceException();
        }
        operationalRepository.markSuccess(requestDto.getTxnId(),OperationType.CREDIT);
    }

    @Transactional
    public void refund(OperationalRequestDto requestDto){

        try {
            var operation = accountMapper.operationalEntity(requestDto);
            operation.setTxnId(requestDto.getTxnId());
            operation.setOperationType(OperationType.REFUND);
            operation.setAmount(requestDto.getAmount());

            operationalRepository.save(operation);
        }catch (DataIntegrityViolationException e){
            return;
        }

        int updated = accountRepository.creditBalance(requestDto.getAccountNumber(), requestDto.getAmount());

        if(updated == 0){
            throw new BalanceException();
        }
        operationalRepository.markSuccess(requestDto.getTxnId(),OperationType.REFUND);
    }

}

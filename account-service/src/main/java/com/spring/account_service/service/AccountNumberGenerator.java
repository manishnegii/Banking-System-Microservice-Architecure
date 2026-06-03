package com.spring.account_service.service;

import com.spring.account_service.entity.AccountNumberSequence;
import com.spring.account_service.entity.AccountType;
import com.spring.account_service.entity.Branch;
import com.spring.account_service.exception.EntityNotFoundException;
import com.spring.account_service.repository.AccountNumberSequenceRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private static final long BLOCK_SIZE = 1000;
    private final AccountNumberSequenceRepository sequenceRepository;

    @Transactional
    public String generate(Branch branch, AccountType accountType
    ) {
        for (int retry = 0; retry < 3; retry++) {
            try {
                return generateInternal(branch, accountType);
            } catch (OptimisticLockException ex) {
                if (retry == 2) {
                    throw new RuntimeException("Failed to generate account number");
                }
            }
        }
        throw new RuntimeException("Account number generation failed");
    }

    private String generateInternal(Branch branch, AccountType accountType
    ) {
        if (branch == null) {
            throw new EntityNotFoundException("Branch not found");
        }

        String branchCode = branch.getBranchCode().substring(3);
        validate(branchCode);

        AccountNumberSequence sequence = sequenceRepository.findByBranchCodeAndAccountType(branchCode, accountType).orElseGet(() -> {AccountNumberSequence seq = new AccountNumberSequence();
            seq.setBranchCode(branchCode);
            seq.setAccountType(accountType);
            seq.setNextValue(1L);
            seq.setMaxValue(BLOCK_SIZE);
            seq.setActive(true);
            return sequenceRepository.save(seq);
        });
        if (!sequence.isActive()) {
            throw new RuntimeException("Sequence disabled");
        }

        /* Block exhausted */

        if (sequence.getNextValue() > sequence.getMaxValue()) {
            sequence.setMaxValue(sequence.getMaxValue() + BLOCK_SIZE );
        }

        long currentSequence = sequence.getNextValue();
        sequence.setNextValue(currentSequence + 1);
        sequenceRepository.save(sequence);

        /* Prevent overflow */

        if (currentSequence > 99999) {
            throw new RuntimeException("Sequence exhausted");
        }

        String formattedSequence = String.format("%05d", currentSequence );
        String base = branchCode + accountType.getCode() + formattedSequence;
        String checksum = generateLuhnChecksum(base);
        return base + checksum;
    }

    /* Luhn checksum */

    private String generateLuhnChecksum( String number) {

        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));

            if(alternate){
                n *= 2;
            if(n>9){
                n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        int firstDigit = (10 - (sum % 10)) % 10;
        int secondDigit = (firstDigit + sum) % 10;
        return "" + firstDigit + secondDigit;
    }

    private void validate( String branchCode ) {
        if (!branchCode.matches("\\d{3}")) {
            throw new IllegalArgumentException(
                    "Invalid branch code"
            );
        }
    }
}

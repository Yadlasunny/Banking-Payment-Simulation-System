package com.banking.system.service;

import com.banking.system.dto.TransactionResponse;
import com.banking.system.entity.Transaction;
import com.banking.system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for querying transaction history.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Retrieves the full transaction history for a given account.
     * An account may appear as the sender or receiver of a transaction.
     *
     * @param accountId the account ID
     * @return list of transaction details ordered by most recent first
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** Maps a Transaction entity to a TransactionResponse DTO */
    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .fromAccountNumber(transaction.getFromAccount() != null
                        ? transaction.getFromAccount().getAccountNumber() : null)
                .toAccountNumber(transaction.getToAccount() != null
                        ? transaction.getToAccount().getAccountNumber() : null)
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}

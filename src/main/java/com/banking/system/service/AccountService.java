package com.banking.system.service;

import com.banking.system.dto.*;
import com.banking.system.entity.*;
import com.banking.system.exception.AccountNotFoundException;
import com.banking.system.exception.InsufficientBalanceException;
import com.banking.system.repository.AccountRepository;
import com.banking.system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service layer for account-related operations: creation, deposit, withdrawal, transfer.
 * All monetary operations are wrapped in @Transactional to guarantee atomicity.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    // ==================== Account Creation ====================

    /**
     * Creates a new bank account for an existing user.
     * Generates a unique 10-digit account number.
     *
     * @param request the account creation request containing the user ID
     * @return the newly created account details
     */
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        User user = userService.getUserById(request.getUserId());

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);
        return mapToResponse(savedAccount);
    }

    // ==================== Deposit ====================

    /**
     * Deposits money into an account.
     * Creates a SUCCESS transaction record.
     *
     * @param request the deposit request
     * @return the transaction details
     */
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Account account = findAccountByNumber(request.getAccountNumber());

        // Credit the account
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Record the transaction
        Transaction transaction = Transaction.builder()
                .toAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    // ==================== Withdrawal ====================

    /**
     * Withdraws money from an account after validating sufficient balance.
     * Records a FAILED transaction if balance is insufficient.
     *
     * @param request the withdrawal request
     * @return the transaction details
     * @throws InsufficientBalanceException if the account lacks sufficient funds
     */
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = findAccountByNumber(request.getAccountNumber());

        // Validate sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            // Record the failed transaction
            Transaction failedTx = Transaction.builder()
                    .fromAccount(account)
                    .amount(request.getAmount())
                    .type(TransactionType.WITHDRAW)
                    .status(TransactionStatus.FAILED)
                    .build();
            transactionRepository.save(failedTx);

            throw new InsufficientBalanceException(
                    request.getAccountNumber(), request.getAmount(), account.getBalance());
        }

        // Debit the account
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Record the successful transaction
        Transaction transaction = Transaction.builder()
                .fromAccount(account)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.SUCCESS)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    // ==================== Transfer ====================

    /**
     * Transfers money between two accounts atomically.
     * Validates both accounts exist and the source has sufficient funds.
     * The entire operation is wrapped in a single transaction for atomicity.
     *
     * @param request the transfer request
     * @return the transaction details
     * @throws AccountNotFoundException   if either account does not exist
     * @throws InsufficientBalanceException if the source account lacks funds
     * @throws IllegalArgumentException    if source and destination are the same
     */
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        // Prevent self-transfer
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Account fromAccount = findAccountByNumber(request.getFromAccountNumber());
        Account toAccount = findAccountByNumber(request.getToAccountNumber());

        // Validate sufficient balance in source account
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            // Record the failed transaction
            Transaction failedTx = Transaction.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .amount(request.getAmount())
                    .type(TransactionType.TRANSFER)
                    .status(TransactionStatus.FAILED)
                    .build();
            transactionRepository.save(failedTx);

            throw new InsufficientBalanceException(
                    request.getFromAccountNumber(), request.getAmount(), fromAccount.getBalance());
        }

        // Debit source account and credit destination account
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Record the successful transaction
        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return mapToTransactionResponse(saved);
    }

    // ==================== Helpers ====================

    /**
     * Finds an account by its account number or throws AccountNotFoundException.
     */
    private Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("accountNumber", accountNumber));
    }

    /**
     * Generates a unique 10-digit account number using UUID.
     * Re-generates if a collision is detected (extremely unlikely).
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 10);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    /** Maps an Account entity to an AccountResponse DTO */
    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .build();
    }

    /** Maps a Transaction entity to a TransactionResponse DTO */
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
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

package com.banking.system.controller;

import com.banking.system.dto.*;
import com.banking.system.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for account management and banking operations.
 * Handles account creation, deposits, withdrawals, and transfers.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * POST /api/accounts — Create a new bank account for an existing user.
     *
     * @param request validated account creation payload
     * @return 201 Created with the new account details
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * POST /api/accounts/deposit — Deposit money into an account.
     *
     * @param request validated deposit payload
     * @return 200 OK with the transaction details
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = accountService.deposit(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/accounts/withdraw — Withdraw money from an account.
     *
     * @param request validated withdrawal payload
     * @return 200 OK with the transaction details
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = accountService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/accounts/transfer — Transfer money between two accounts.
     *
     * @param request validated transfer payload
     * @return 200 OK with the transaction details
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = accountService.transfer(request);
        return ResponseEntity.ok(response);
    }
}

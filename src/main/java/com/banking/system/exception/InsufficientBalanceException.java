package com.banking.system.exception;

/**
 * Thrown when an account has insufficient funds for a withdrawal or transfer.
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String accountNumber, java.math.BigDecimal requested, java.math.BigDecimal available) {
        super(String.format("Insufficient balance in account '%s'. Requested: %s, Available: %s",
                accountNumber, requested, available));
    }
}

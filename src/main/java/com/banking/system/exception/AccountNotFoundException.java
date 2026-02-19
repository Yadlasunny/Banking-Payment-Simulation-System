package com.banking.system.exception;

/**
 * Thrown when the requested account does not exist in the system.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String fieldName, String fieldValue) {
        super(String.format("Account not found with %s: '%s'", fieldName, fieldValue));
    }
}

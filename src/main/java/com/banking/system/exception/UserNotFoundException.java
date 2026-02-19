package com.banking.system.exception;

/**
 * Thrown when the requested user does not exist in the system.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with id: %d", userId));
    }
}

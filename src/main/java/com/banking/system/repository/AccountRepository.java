package com.banking.system.repository;

import com.banking.system.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Account entity CRUD operations.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /** Find an account by its unique account number */
    Optional<Account> findByAccountNumber(String accountNumber);

    /** Check if an account number already exists */
    boolean existsByAccountNumber(String accountNumber);
}

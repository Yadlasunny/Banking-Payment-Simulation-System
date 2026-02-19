package com.banking.system.repository;

import com.banking.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Check if a user already exists with the given email */
    boolean existsByEmail(String email);

    /** Find a user by their email address */
    Optional<User> findByEmail(String email);
}

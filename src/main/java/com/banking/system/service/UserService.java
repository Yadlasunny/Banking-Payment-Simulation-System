package com.banking.system.service;

import com.banking.system.dto.UserRequest;
import com.banking.system.dto.UserResponse;
import com.banking.system.entity.User;
import com.banking.system.exception.UserNotFoundException;
import com.banking.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for user-related business logic.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user after validating email uniqueness.
     *
     * @param request the user creation request
     * @return the newly created user's details
     * @throws IllegalArgumentException if the email is already registered
     */
    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the user ID
     * @return the user entity
     * @throws UserNotFoundException if the user does not exist
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /** Maps a User entity to a UserResponse DTO */
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

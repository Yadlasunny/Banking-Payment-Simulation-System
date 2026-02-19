package com.banking.system.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO exposing user details to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}

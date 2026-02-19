package com.banking.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for creating a new bank account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotNull(message = "User ID is required")
    private Long userId;
}

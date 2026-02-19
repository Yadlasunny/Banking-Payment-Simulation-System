package com.banking.system.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Response DTO exposing account details to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;
    private String userName;
}

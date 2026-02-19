package com.banking.system.exception;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response body returned by the global exception handler.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
}

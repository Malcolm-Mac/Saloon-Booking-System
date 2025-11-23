package com.medelin.dto;

import com.medelin.util.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank(message = "Token is required")
        String token,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = ValidationPatterns.PASSWORD_PATTERN,
                message = "Password must be at least 8 characters long, include upper and lower case letters, a number, and a special character"
        )
        String newPassword
) { }

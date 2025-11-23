package com.medelin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to reset a user's password")
public record ForgotPasswordRequest(
        @Schema(description = "User's email address", example="john.doe@example.com")
        @NotBlank(message="Email is required")
        @Email(message = "Invalid email address format")
        String email
){}

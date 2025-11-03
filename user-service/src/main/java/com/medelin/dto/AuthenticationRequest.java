package com.medelin.dto;


import com.medelin.util.UniqueEmail;
import com.medelin.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to authenticate a user")
public record AuthenticationRequest(
    @Schema(description = "User's email address", example="john.doe@example.com")
    @NotBlank(message="Email is required")
    @UniqueEmail
    String email,

    @Schema(description = "User's password (min 8 characters)", example = "SecurePass123!")
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = ValidationPatterns.PASSWORD_PATTERN,
            message = "Password must be at least 8 characters long, include upper and lower case letters, a number, and a special character"
    )
    String password)
{
}

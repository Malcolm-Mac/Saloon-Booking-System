package com.medelin.dto;


import com.medelin.util.UniqueEmail;
import com.medelin.util.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to authenticate a user")
public record AuthenticationRequest(
    @Schema(description = "User's email address", example="john.doe@example.com")
    @NotBlank(message="Email is required")
    @UniqueEmail
    String email,

    @Schema(description = "User's password (min 8 characters)", example = "SecurePass123!")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String password)
{
}

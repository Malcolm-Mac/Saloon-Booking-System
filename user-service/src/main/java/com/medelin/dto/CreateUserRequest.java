package com.medelin.dto;

import com.medelin.util.UniqueEmail;
import com.medelin.util.ValidPassword;
import com.medelin.model.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to create a new user")
public record CreateUserRequest(
         @Schema(description="User's full name", example = "John Doe")
         @NotBlank(message="Full name is required")
         String fullName,

         @Schema(description = "User's email address", example="john.doe@example.com")
         @NotBlank(message="Email is required")
         @UniqueEmail
         String email,

         @Schema(description = "User's phone number", example = "+27764567078")
         @NotBlank(message = "Phone number is required")
         String phoneNumber,

         @Schema(description = "User's role",example = "customer")
         @NotNull(message = "Role is required")
         @Enumerated(EnumType.STRING)
         Role role,

         @Schema(description = "User's password (min 8 characters)", example = "SecurePass123!")
         @NotBlank(message = "Password is required")
         @ValidPassword
         String password
) { }

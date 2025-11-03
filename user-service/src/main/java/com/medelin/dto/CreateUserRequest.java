package com.medelin.dto;

import com.medelin.util.UniqueEmail;
import com.medelin.model.enumeration.Role;
import com.medelin.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to create a new user")
public record CreateUserRequest(
         @Schema(description="User's full name", example = "John Doe")
         @NotBlank(message="Full name is required")
         @Pattern(
                 regexp = ValidationPatterns.NAME_PATTERN,
                 message = "Invalid name format"
         )
         String fullName,

         @Schema(description = "User's email address", example="john.doe@example.com")
         @NotBlank(message="Email is required")
         @UniqueEmail
         @Email(message = "Invalid email address format")
         String email,

         @Schema(description = "User's phone number", example = "+27764567078")
         @NotBlank(message = "Phone number is required")
         @Pattern(
                 regexp = ValidationPatterns.PHONE_PATTERN,
                 message = "Phone number must be in valid E.164 format (e.g. +27694749629)"
         )
         String phoneNumber,

         @Schema(description = "User's role",example = "customer")
         @NotNull(message = "Role is required")
         @Enumerated(EnumType.STRING)
         Role role,

         @Schema(description = "User's password (min 8 characters)", example = "SecurePass123!")
         @NotBlank(message = "Password is required")
         @Pattern(
                 regexp = ValidationPatterns.PASSWORD_PATTERN,
                 message = "Password must be at least 8 characters long, include upper and lower case letters, a number, and a special character"
         )
         String password
) { }

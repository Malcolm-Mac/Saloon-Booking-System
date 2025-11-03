package com.medelin.dto;

import com.medelin.model.enumeration.Role;
import com.medelin.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request to update user details")
public record UpdateUserRequest(
                @Schema(description = "User's full name", example = "John Doe")
                @NotBlank(message="Full name is required")
                @Pattern(
                        regexp = ValidationPatterns.NAME_PATTERN,
                        message = "Invalid name format"
                )
                String fullName,

                @Schema(description = "User's phone number", example = "+27 69 474 9629")
                @NotBlank(message="Phone number is required")
                @Pattern(
                        regexp = ValidationPatterns.PHONE_PATTERN,
                        message = "Phone number must be in valid E.164 format (e.g. +27694749629)"
                )
                String phoneNumber,

                @Schema(description = "User's role", example = "Customer")
                @NotBlank(message="Role is required")
                @Enumerated(EnumType.STRING)
                Role role
) { }

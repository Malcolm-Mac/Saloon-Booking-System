package com.medelin.dto;

import com.medelin.model.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to update user details")
public record UpdateUserRequest(
                @Schema(description = "User's full name", example = "John Doe")
                @NotBlank(message="Full name is required")
                String fullName,

                @Schema(description = "User's phone number", example = "+27 69 474 9629")
                @NotBlank(message="Phone number is required")
                String phoneNumber,

                @Schema(description = "User's role", example = "Customer")
                @NotBlank(message="Role is required")
                @Enumerated(EnumType.STRING)
                Role role
) { }

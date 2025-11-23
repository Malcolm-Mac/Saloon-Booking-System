package com.medelin.dto;

import com.medelin.model.User;
import com.medelin.model.enumeration.Role;

import java.time.LocalDateTime;

public record UserInfoResponse(
        String fullName,
        String email,
        String phoneNumber,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{
    public static UserInfoResponse from(User user)
    {
        return new UserInfoResponse(
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

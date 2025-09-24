package com.medelin.dto;

import com.medelin.util.IdHasherUtil;
import com.medelin.model.enumeration.Role;
import com.medelin.model.User;

import java.time.LocalDateTime;

public record UserDetailResponse(
        String id,
        String fullName,
        String email,
        String phoneNumber,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{
    public static UserDetailResponse from(User user, IdHasherUtil idHasherUtil)
    {
        return new UserDetailResponse(
                idHasherUtil.encode(user.getId()),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

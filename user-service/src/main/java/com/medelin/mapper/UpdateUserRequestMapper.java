package com.medelin.mapper;

import com.medelin.dto.UpdateUserRequest;
import com.medelin.model.User;
import com.medelin.model.enumeration.Role;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserRequestMapper
{
    public User toEntity(UpdateUserRequest request)
    {
        User user = new User();
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(Role.valueOf(request.role().name().toUpperCase()));
        return user;
    }
}

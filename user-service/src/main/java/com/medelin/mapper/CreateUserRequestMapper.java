package com.medelin.mapper;

import com.medelin.dto.CreateUserRequest;
import com.medelin.model.enumeration.Role;
import com.medelin.model.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestMapper
{
    public User toEntity(CreateUserRequest request, String encodedPassword)
    {
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(Role.valueOf(request.role().name().toUpperCase()));
        user.setPassword(encodedPassword);
        return user;
    }
}

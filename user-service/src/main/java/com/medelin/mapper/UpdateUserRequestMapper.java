package com.medelin.mapper;

import com.medelin.dto.UpdateUserRequest;
import com.medelin.model.User;
import com.medelin.model.enumeration.Role;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserRequestMapper
{
    public void updateEntity(UpdateUserRequest request, User user) {
        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }
        if (request.role() != null) {
            user.setRole(Role.valueOf(request.role().name().toUpperCase()));
        }
    }
}

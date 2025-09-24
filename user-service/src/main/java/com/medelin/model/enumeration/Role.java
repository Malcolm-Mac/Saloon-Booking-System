package com.medelin.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role
{
    ADMIN,
    CUSTOMER,
    SUPER_ADMIN;

    @JsonCreator
    public static Role fromString(String value)
    {
        return Role.valueOf(value.toUpperCase());
    }
}

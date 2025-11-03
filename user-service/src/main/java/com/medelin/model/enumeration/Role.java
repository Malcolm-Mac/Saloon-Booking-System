package com.medelin.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role
{
    ADMIN,
    CUSTOMER,
    BARBER,
    SUPER_ADMIN;

    @JsonCreator
    public static Role fromString(String value)
    {
        try
        {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex)
        {
            throw new IllegalArgumentException(
                    "Invalid role: " + value + ". Valid roles are: " + java.util.Arrays.toString(Role.values())
            );
        }
    }
}

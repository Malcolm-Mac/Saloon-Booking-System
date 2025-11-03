package com.medelin.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationPatterns
{
    public static final String NAME_PATTERN ="^[A-Za-z]+(?: [A-Za-z]+)*$";
    public static final String PHONE_PATTERN ="^\\+[1-9]\\d{1,14}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,20}$";
}

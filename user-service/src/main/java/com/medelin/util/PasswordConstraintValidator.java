package com.medelin.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String>
{
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!]).{8,20}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context)
    {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}

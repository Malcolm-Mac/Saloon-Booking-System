package com.medelin.util;

import com.medelin.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>
{
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context)
    {
        return email == null || !userRepository.existsByEmail(email);
    }
}

package ru.practicum.mainsvc.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankNullableValidator implements ConstraintValidator<NotBlankNullable, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return true;
        else
            return !s.isBlank();
    }
}

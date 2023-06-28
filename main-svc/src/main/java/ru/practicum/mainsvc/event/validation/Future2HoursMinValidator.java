package ru.practicum.mainsvc.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDateTime;

public class Future2HoursMinValidator implements ConstraintValidator<Future2HoursMin, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime time, ConstraintValidatorContext constraintValidatorContext) {
        if (time == null)
            return true;
        LocalDateTime validFuturePoint = LocalDateTime.now();
        Duration gap = Duration.between(validFuturePoint, time);
        return !gap.isNegative();
    }
}

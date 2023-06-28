package ru.practicum.mainsvc.event.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Future2HoursMinValidator.class)
public @interface Future2HoursMin {
    String message() default "Time and date must be later then 2 hours from now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

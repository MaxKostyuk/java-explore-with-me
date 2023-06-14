package ru.practicum.mainsvc.event.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankNullableValidator.class)
public @interface NotBlankNullable {
    String message() default "Field must not consist only of spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

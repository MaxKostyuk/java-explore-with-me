package ru.practicum.mainsvc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    @NotNull
    @Positive
    private Long id;
    @Length(min = 2, max = 250, message = "Name length must be between 2 and 250 characters")
    private String name;
    @Email(message = "Must be valid email address")
    @Length(min = 6, max = 254, message = "Email length must be between 6 and 254 characters")
    private String email;
}

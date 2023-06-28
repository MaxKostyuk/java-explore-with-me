package ru.practicum.mainsvc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserRequest {
    @NotBlank(message = "Name must contain symbols besides spaces")
    @Length(min = 2, max = 250, message = "Name length must be between 2 and 250 characters")
    private String name;
    @NotNull(message = "Field email must be field")
    @Email(message = "Must be valid email address")
    @Length(min = 6, max = 254, message = "Email length must be between 6 and 254 characters")
    private String email;
}

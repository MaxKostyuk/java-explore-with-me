package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsvc.event.validation.Future2HoursMin;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Annotation length must be between 20 and 2000 characters")
    private String annotation;
    @NotNull
    @Positive
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Description length must be between 20 and 7000 characters")
    private String description;
    @NotNull
    @Future2HoursMin
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120, message = "Title length must be between 3 and 120 characters")
    private String title;
}

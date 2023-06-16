package ru.practicum.mainsvc.event.dto;

import lombok.Data;
import ru.practicum.mainsvc.event.validation.Future2HoursMin;
import ru.practicum.mainsvc.event.validation.NotBlankNullable;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public abstract class UpdateEventAbstractRequest {
    @NotBlankNullable
    @Size(min = 20, max = 2000, message = "Annotation length must be between 20 and 2000 characters")
    private String annotation;
    @Positive
    private Long category;
    @NotBlankNullable
    @Size(min = 20, max = 7000, message = "Description length must be between 20 and 7000 characters")
    private String description;
    @Future2HoursMin
    private LocalDateTime eventDate;
    @Valid
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlankNullable
    @Size(min = 3, max = 120, message = "Title length must be between 3 and 120 characters")
    private String title;
}

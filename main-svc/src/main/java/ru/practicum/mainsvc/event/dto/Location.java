package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {
    @NotNull
    @Min(value = -90, message = "Latitude must be more than -90 degrees")
    @Max(value = 90, message = "Latitude must be less than 90 degrees")
    private Double lat;
    @NotNull
    @Min(value = -180, message = "Longitude must be more than -180 degrees")
    @Max(value = 180, message = "Longitude must be less than 180 degrees")
    private Double lon;
}

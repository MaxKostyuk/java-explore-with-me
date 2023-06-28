package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsvc.category.dto.CategoryDto;
import ru.practicum.mainsvc.user.dto.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventShortDto {
    private Long id;
    private UserShortDto initiator;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime eventDate;
    private Boolean paid;
    private String title;
    private Long views;
}

package ru.practicum.mainsvc.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsvc.event.validation.NotBlankNullable;

import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlankNullable
    @Size(min = 1, max = 50)
    private String title;
}

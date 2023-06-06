package ru.practicum.statdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatViewDto {
    private String app;
    private String uri;
    private Long hits;
}

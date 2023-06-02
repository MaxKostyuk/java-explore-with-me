package ru.pracricum.statserver.service;

import ru.practicum.statdto.dto.StatHitDto;
import ru.practicum.statdto.dto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void saveStat(StatHitDto statHit);

    List<StatViewDto> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}

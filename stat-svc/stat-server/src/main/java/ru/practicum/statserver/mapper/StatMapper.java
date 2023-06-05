package ru.practicum.statserver.mapper;

import ru.practicum.statserver.model.StatRecord;
import ru.practicum.statdto.StatHitDto;

public class StatMapper {

    public static StatRecord toStatRecord(StatHitDto statHit) {
        return StatRecord.builder()
                .app(statHit.getApp())
                .uri(statHit.getUri())
                .ip(statHit.getIp())
                .created(statHit.getTimestamp())
                .build();
    }
}

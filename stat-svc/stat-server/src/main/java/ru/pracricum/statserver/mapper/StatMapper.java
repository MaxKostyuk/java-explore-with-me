package ru.pracricum.statserver.mapper;

import ru.pracricum.statserver.model.StatRecord;
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

package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statserver.service.StatService;
import ru.practicum.statdto.StatHitDto;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStatistics(@RequestBody StatHitDto statHit) {
        log.info("Creating stat record for app={} and url={}, ip={}, timestamp={}",
                statHit.getApp(), statHit.getUri(), statHit.getIp(), statHit.getTimestamp());
        service.saveStat(statHit);
    }

    @GetMapping("/stats")
    public List<StatViewDto> getStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Statistics request with start={}, end={}, uris={} and unique={}", start, end, uris, unique);
        return service.getStatistics(start, end, uris, unique);
    }
}

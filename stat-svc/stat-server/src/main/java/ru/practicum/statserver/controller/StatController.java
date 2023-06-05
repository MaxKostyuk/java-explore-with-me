package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statserver.service.StatService;
import ru.practicum.statdto.StatHitDto;
import ru.practicum.statdto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStatistics(@RequestBody StatHitDto statHit) {
        //надо ли логгировать?
        service.saveStat(statHit);
    }

    @GetMapping("/stats")
    public List<StatViewDto> getStatistics(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(required = false, defaultValue = "false") boolean unique) {
        //логгируем?
        return service.getStatistics(start, end, uris, unique);
    }
}

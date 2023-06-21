package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.enums.EventSearchSort;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    private final EventService service;

    @GetMapping
    public List<EventShortDto> publicSearch(@RequestParam String text,
                                            @RequestParam Long[] categories,
                                            @RequestParam Boolean paid,
                                            @RequestParam LocalDateTime rangeStart,
                                            @RequestParam LocalDateTime rangeEnd,
                                            @RequestParam Boolean onlyAvailable,
                                            @RequestParam EventSearchSort sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size,
                                            HttpServletRequest request) {
        return service.publicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());
    }

    @GetMapping("/{eventId}")
    public EventFullDto publicGetById(@PathVariable @Positive Long eventId,
                                      HttpServletRequest request) {
        return service.publicGetById(eventId, request.getRemoteAddr());
    }
}

package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainsvc.event.enums.EventState;
import ru.practicum.mainsvc.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService service;

    @GetMapping
    public List<EventFullDto> adminSearchEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<EventState> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("EventAdminController - adminSearchEvents - users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.adminSearchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto adminUpdateEvent(@RequestBody @Valid UpdateEventAdminRequest eventRequest,
                                         @PathVariable @Positive Long eventId) {
        log.info("EventAdminController - adminUpdateEvent - eventId = {}, eventRequest = {}", eventId, eventRequest);
        return service.adminUpdateEvent(eventId, eventRequest);
    }

}

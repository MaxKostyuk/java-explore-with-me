package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.enums.EventState;
import ru.practicum.mainsvc.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainsvc.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService service;

    @GetMapping
    public List<EventFullDto> adminSearchEvents(@RequestParam Long[] users,
                                                @RequestParam EventState[] states,
                                                @RequestParam Long[] categories,
                                                @RequestParam LocalDateTime rangeStart,
                                                @RequestParam LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return service.adminSearchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto adminUpdateEvent(@RequestBody @Valid UpdateEventAdminRequest eventRequest,
                                         @PathVariable @Positive Long eventId) {
        return service.adminUpdateEvent(eventId, eventRequest);
    }

}

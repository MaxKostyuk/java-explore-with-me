package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.NewEventDto;
import ru.practicum.mainsvc.event.dto.UpdateEventUserRequest;
import ru.practicum.mainsvc.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@RequestBody @Valid NewEventDto newEvent,
                                 @PathVariable @Positive Long userId) {
        return service.addEvent(newEvent, userId);
    }

    @GetMapping
    public List<EventFullDto> getUsersEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return service.getUsersEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId) {
        return service.getEventById(eventId);
    }

    //добавить логгирование во все контроллеры!
    //проверить все контроллеры на наличие validated и positve для всех id!

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@RequestBody @Valid UpdateEventUserRequest newEvent,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        return service.updateEvent(eventId, userId, newEvent);
    }

}

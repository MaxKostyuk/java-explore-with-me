package ru.practicum.mainsvc.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.dto.NewEventDto;
import ru.practicum.mainsvc.event.dto.UpdateEventUserRequest;
import ru.practicum.mainsvc.event.service.EventService;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@RequestBody @Valid NewEventDto newEvent,
                                 @PathVariable @Positive Long userId) {
        log.info("EventPrivateController - addEvent - userId = {}, newEvent = {}", userId, newEvent);
        return service.addEvent(newEvent, userId);
    }

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable @Positive Long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("EventPrivateController - getUsersEvents - userId = {}, from = {}, size = {}", userId, from, size);
        return service.getUsersEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable @Positive Long eventId,
                                 @PathVariable @Positive Long userId) {
        log.info("EventPrivateController - getEvent - userId = {}, eventId = {}", userId, eventId);
        return service.getEventById(eventId, userId);
    }


    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@RequestBody @Valid UpdateEventUserRequest newEvent,
                                    @PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId) {
        log.info("EventPrivateController - updateEvent - userId = {}, eventId = {}, newEvent = {}", userId, eventId, newEvent);
        return service.updateEvent(eventId, userId, newEvent);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventsRequests(@PathVariable @Positive Long userId,
                                                           @PathVariable @Positive Long eventId) {
        log.info("EventPrivateController - getEventsRequests - userId = {}, eventId = {}", userId, eventId);
        return service.getEventsRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsStatus(@PathVariable @Positive Long userId,
                                                                    @PathVariable @Positive Long eventId,
                                                                    @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        log.info("EventPrivateController - updateEventRequestsStatus - userId = {}, eventId = {}, updateRequest = {}", userId, eventId, updateRequest);
        return service.updateParticipationRequests(userId, eventId, updateRequest);
    }
}

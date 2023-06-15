package ru.practicum.mainsvc.event.service;

import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.NewEventDto;
import ru.practicum.mainsvc.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEvent, Long userId);

    List<EventFullDto> getUsersEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long eventId);

    EventFullDto updateEvent(Long eventId, Long userId, UpdateEventUserRequest newEvent);
}

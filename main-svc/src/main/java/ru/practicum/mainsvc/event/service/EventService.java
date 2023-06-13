package ru.practicum.mainsvc.event.service;

import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.NewEventDto;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEvent, Long userId);
}

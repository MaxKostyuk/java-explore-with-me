package ru.practicum.mainsvc.event.repository;

import ru.practicum.mainsvc.event.model.Event;

public interface EventRepositoryCustom {

    Event getEventById(Long eventId);
}

package ru.practicum.mainsvc.event.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.exception.ElementNotFoundException;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final EventRepository repository;
    private static final String EVENT_NOT_FOUND_TEMPLATE = "Event with id %d not found";

    public EventRepositoryCustomImpl(@Lazy EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Event getEventById(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new ElementNotFoundException(String.format(EVENT_NOT_FOUND_TEMPLATE, eventId)));
    }
}

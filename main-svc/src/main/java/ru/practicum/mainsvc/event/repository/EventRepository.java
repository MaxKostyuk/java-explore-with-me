package ru.practicum.mainsvc.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}

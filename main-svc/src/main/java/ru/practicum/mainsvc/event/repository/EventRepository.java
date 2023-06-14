package ru.practicum.mainsvc.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    List<Event> findAllByInitiatorId(Long initiator, Pageable page);
}

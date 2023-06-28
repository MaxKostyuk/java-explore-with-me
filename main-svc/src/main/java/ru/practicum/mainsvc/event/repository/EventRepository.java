package ru.practicum.mainsvc.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.event.enums.EventState;
import ru.practicum.mainsvc.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    List<Event> findAllByInitiatorId(Long initiator, Pageable page);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate < :rangeEnd)")
    List<Event> searchEvents(List<Long> users, List<EventState> states, List<Long> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("select e from Event e " +
            "where e.state = 'PUBLISHED' " +
            "and (:text is null or upper(e.annotation) like upper(concat('%', :text, '%')) or upper(e.description) like upper(concat('%', :text, '%')))" +
            "and (:categories is null or e.category.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (cast(:rangeStart as java.time.LocalDateTime) is null or e.eventDate > :rangeStart) " +
            "and (cast(:rangeEnd as java.time.LocalDateTime) is null or e.eventDate < :rangeEnd) " +
            "and (:onlyAvailable is null or e.confirmedRequests < e.participantLimit or e.participantLimit = 0)")
    List<Event> publicSearch(String text, List<Long> categories, Boolean paid,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, PageRequest id);
}

package ru.practicum.mainsvc.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.event.dto.EventState;
import ru.practicum.mainsvc.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    List<Event> findAllByInitiatorId(Long initiator, Pageable page);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category in :categories) " +
            "and (:rangeStart is null or e.eventDate > :rangeStart) " +
            "and (:rangeEnd is null or e.eventDate < :rangeEnd)")
    List<Event> searchEvents(Long[] users, EventState[] states, Long[] categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("select e from Event e " +
            "where e.state = 'PUBLISHED' " +
            "and (:text is null or upper(e.annotation) like upper(concat('%', :text, '%')) or upper(e.description) like upper(concat('%', :text, '%')))" +
            "and (:categories is null or e.category in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (:rangeStart is null or e.eventDate > :rangeStart) " +
            "and (:rangeEnd is null or e.eventDate < :rangeEnd)")
        //тут еще добавить строку про онли авейлабл
    List<Event> publicSearch(String text, Long[] categories, Boolean paid,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, PageRequest id);
}

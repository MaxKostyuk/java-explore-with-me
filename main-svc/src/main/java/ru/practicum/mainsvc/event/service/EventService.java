package ru.practicum.mainsvc.event.service;

import ru.practicum.mainsvc.event.dto.*;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEvent, Long userId);

    List<EventFullDto> getUsersEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long eventId);

    EventFullDto updateEvent(Long eventId, Long userId, UpdateEventUserRequest newEvent);

    List<EventFullDto> adminSearchEvents(Long[] users, EventState[] states, Long[] categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest eventRequest);

    List<EventFullDto> publicSearch(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearchSort sort, Integer from, Integer size);

    EventFullDto publicGetById(Long eventId);

    List<ParticipationRequestDto> getEventsRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}

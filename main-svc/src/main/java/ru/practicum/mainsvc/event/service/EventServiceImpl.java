package ru.practicum.mainsvc.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.event.dto.*;
import ru.practicum.mainsvc.event.enums.AdminStateAction;
import ru.practicum.mainsvc.event.enums.EventSearchSort;
import ru.practicum.mainsvc.event.enums.EventState;
import ru.practicum.mainsvc.event.enums.UserStateAction;
import ru.practicum.mainsvc.event.mapper.EventMapper;
import ru.practicum.mainsvc.event.mapper.LocationMapper;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.exception.ActionForbiddenException;
import ru.practicum.mainsvc.exception.ElementNotFoundException;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainsvc.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;
import ru.practicum.mainsvc.request.enums.RequestStatus;
import ru.practicum.mainsvc.request.mapper.RequestMapper;
import ru.practicum.mainsvc.request.model.ParticipationRequest;
import ru.practicum.mainsvc.request.repository.RequestRepository;
import ru.practicum.mainsvc.stats.service.StatService;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatService statService;

    @Override
    public EventFullDto addEvent(NewEventDto newEvent, Long userId) {
        User user = userRepository.getUserById(userId);
        Category category = categoryRepository.getCategoryById(newEvent.getCategory());
        Event event = EventMapper.toEvent(newEvent);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(LocationMapper.toLocation(newEvent.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        EventFullDto createdEvent = EventMapper.toEventFullDto(eventRepository.save(event));
        createdEvent.setConfirmedRequests(0);
        createdEvent.setViews(0L);
        return createdEvent;
    }

    @Override
    public List<EventShortDto> getUsersEvents(Long userId, Integer from, Integer size) {
        List<EventShortDto> foundEvents = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size, Sort.by("id")))
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return setViews(foundEvents);
    }

    @Override
    public EventFullDto getEventById(Long eventId, Long userId) {
        userRepository.getUserById(userId);
        EventFullDto eventToReturn = EventMapper.toEventFullDto(eventRepository.getEventById(eventId));
        if (!eventToReturn.getInitiator().getId().equals(userId))
            throw new ActionForbiddenException("This option is available only for initiator of event");
        eventToReturn.setViews(statService.getViews(eventId));
        return eventToReturn;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, Long userId, UpdateEventUserRequest newEvent) {
        userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (event.getState() == EventState.PUBLISHED)
            throw new DataIntegrityViolationException("Only pending or canceled events can be changed");
        updateEventFields(newEvent, event);
        if (Objects.nonNull(newEvent.getStateAction()))
            event.setState((newEvent.getStateAction() == UserStateAction.SEND_TO_REVIEW ?
                    EventState.PENDING : EventState.CANCELED));
        EventFullDto updatedEvent = EventMapper.toEventFullDto(eventRepository.save(event));
        updatedEvent.setViews(statService.getViews(updatedEvent.getId()));
        return updatedEvent;
    }

    @Override
    public List<EventFullDto> adminSearchEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<EventFullDto> foundEvents = eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, page)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
        return setViewsFull(foundEvents);
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest eventRequest) {
        Event event = eventRepository.getEventById(eventId);
        updateEventFields(eventRequest, event);
        LocalDateTime minStartTime = LocalDateTime.now().plusHours(1);
        if (Duration.between(minStartTime, event.getEventDate()).isNegative())
            throw new IllegalArgumentException("Event start time must be at least one hour later from now");
        if (eventRequest.getStateAction() != null) {
            if (eventRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING)
                    throw new DataIntegrityViolationException("Only pending events may by published");
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                if (event.getState() == EventState.PUBLISHED)
                    throw new DataIntegrityViolationException("Published events cannot be rejected");
                event.setState(EventState.CANCELED);
            }
        }
        EventFullDto updatedEvent = EventMapper.toEventFullDto(eventRepository.save(event));
        updatedEvent.setViews(statService.getViews(updatedEvent.getId()));
        return updatedEvent;
    }

    @Override
    public List<EventShortDto> publicSearch(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearchSort sort, Integer from, Integer size, String ip) {
        if (rangeStart == null & rangeEnd == null)
            rangeStart = LocalDateTime.now();
        if (rangeEnd != null && rangeStart != null && !rangeEnd.isAfter(rangeStart))
            throw new IllegalArgumentException("RangeEnd must be after rangeStart");
        List<EventShortDto> foundEvents = eventRepository.publicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        PageRequest.of(from, size, Sort.by("id")))
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        statService.saveViews(0L, ip);
        setViews(foundEvents);
        if (sort != null) {
            if (sort == EventSearchSort.EVENT_DATE) {
                foundEvents = foundEvents.stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());
            } else {
                foundEvents = foundEvents.stream().sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
            }
        }
        return foundEvents;
    }

    @Override
    public EventFullDto publicGetById(Long eventId, String ip) {
        Event event = eventRepository.getEventById(eventId);
        if (event.getState() != EventState.PUBLISHED)
            throw new ElementNotFoundException("Event with id " + eventId + " not found");
        EventFullDto eventToReturn = EventMapper.toEventFullDto(event);
        statService.saveViews(eventId, ip);
        eventToReturn.setViews(statService.getViews(eventId));
        return eventToReturn;
    }

    @Override
    public List<ParticipationRequestDto> getEventsRequests(Long userId, Long eventId) {
        userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new ActionForbiddenException("Only initiator may get event participation requests");
        return requestRepository.findAllByEventId(eventId)
                .stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        if (updateRequest.getStatus() != RequestStatus.CONFIRMED && updateRequest.getStatus() != RequestStatus.REJECTED)
            throw new ActionForbiddenException("New status must be either CONFIRMED or REJECTED");
        userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new ActionForbiddenException("Only initiator may moderate event participation requests");
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0))
            throw new ActionForbiddenException("This event need no participation request moderation");
        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && updateRequest.getStatus() == RequestStatus.CONFIRMED)
            throw new DataIntegrityViolationException("All slots for participants are booked already");

        List<ParticipationRequest> requestsToUpdate = requestRepository.findAllById(updateRequest.getRequestIds());
        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        int availableSlots = event.getParticipantLimit() - event.getConfirmedRequests();

        for (ParticipationRequest request : requestsToUpdate) {
            if (!request.getEvent().getId().equals(eventId))
                throw new ActionForbiddenException("You are moderating a request of other event");
            if (request.getStatus() != RequestStatus.PENDING)
                throw new DataIntegrityViolationException("Only pending participation requests are allowed to moderate");
            if (updateRequest.getStatus() == RequestStatus.CONFIRMED && availableSlots > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                availableSlots--;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }
        requestRepository.saveAll(requestsToUpdate);

        EventRequestStatusUpdateResult moderationResult = new EventRequestStatusUpdateResult();
        moderationResult.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList()));
        moderationResult.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList()));

        return moderationResult;
    }

    private void updateEventFields(UpdateEventAbstractRequest newEvent, Event event) {
        if (Objects.nonNull(newEvent.getCategory()))
            event.setCategory(categoryRepository.getCategoryById(newEvent.getCategory()));
        if (Objects.nonNull(newEvent.getAnnotation()))
            event.setAnnotation(newEvent.getAnnotation());
        if (Objects.nonNull(newEvent.getDescription()))
            event.setDescription(newEvent.getDescription());
        if (Objects.nonNull(newEvent.getEventDate()))
            event.setEventDate(newEvent.getEventDate());
        if (Objects.nonNull(newEvent.getLocation()))
            event.setLocation(LocationMapper.toLocation(newEvent.getLocation()));
        if (Objects.nonNull(newEvent.getPaid()))
            event.setPaid(newEvent.getPaid());
        if (Objects.nonNull(newEvent.getParticipantLimit()))
            event.setParticipantLimit(newEvent.getParticipantLimit());
        if (Objects.nonNull(newEvent.getRequestModeration()))
            event.setRequestModeration(newEvent.getRequestModeration());
        if (Objects.nonNull(newEvent.getTitle()))
            event.setTitle(newEvent.getTitle());
    }

    private List<EventShortDto> setViews(List<EventShortDto> events) {
        Map<Long, Long> views = statService.getViews(events.stream().map(EventShortDto::getId).collect(Collectors.toList()));
        for (EventShortDto event : events) {
            event.setViews(views.get(event.getId()));
        }
        return events;
    }

    private List<EventFullDto> setViewsFull(List<EventFullDto> events) {
        Map<Long, Long> views = statService.getViews(events.stream().map(EventFullDto::getId).collect(Collectors.toList()));
        for (EventFullDto event : events) {
            event.setViews(views.get(event.getId()));
        }
        return events;
    }
}

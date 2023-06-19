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
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

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
        //не хватает заявок и просмотров
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getUsersEvents(Long userId, Integer from, Integer size) {
        //тут не хватает количества просмотров, но они пока не реализованы и заявок
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size, Sort.by("id")))
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        //тоже не хватает подтвержденных заявок и просмотров
        return EventMapper.toEventFullDto(eventRepository.getEventById(eventId));
    }

    @Override
    public EventFullDto updateEvent(Long eventId, Long userId, UpdateEventUserRequest newEvent) {
        userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (event.getState() == EventState.PUBLISHED)
            throw new ActionForbiddenException("Only pending or canceled events can be changed");
        updateEventFields(newEvent, event);
        if (Objects.nonNull(newEvent.getStateAction()))
            event.setState((newEvent.getStateAction() == UserStateAction.SEND_TO_REVIEW ?
                    EventState.PENDING : EventState.CANCELED));
        //опять же не хватает подтвержденных заявок и просмотров
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> adminSearchEvents(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        //добавляем подтвержденные заявки и просмотры
        return eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, page)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
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
                    throw new ActionForbiddenException("Only pending events may by published");
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                if (event.getState() == EventState.PUBLISHED)
                    throw new ActionForbiddenException("Published events cannot be rejected");
                event.setState(EventState.CANCELED);
            }
        }
        //опять же ставим тут данные по просмотрам и подтвержденным заявкам
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> publicSearch(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearchSort sort, Integer from, Integer size) {
        //для каждого события отправить плюс в статистику
        if (rangeStart == null & rangeEnd == null)
            rangeStart = LocalDateTime.now(); //надо убедиться, что этот иммьютабл нормально подменяется, если что
        return eventRepository.publicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        PageRequest.of(from, size, Sort.by("id")))
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        //пока не хватает еще соритровки
    }

    @Override
    public EventFullDto publicGetById(Long eventId) {
        Event event = eventRepository.getEventById(eventId);
        if (event.getState() != EventState.PUBLISHED)
            throw new ElementNotFoundException("Event with id " + eventId + " not found");
        //и что в статистику отправляется плюс
        //ну и по традиции подставить views
        return EventMapper.toEventFullDto(event);
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
        userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new ActionForbiddenException("Only initiator may moderate event participation requests");
        if (!event.getRequestModeration() | event.getParticipantLimit().equals(0))
            throw new ActionForbiddenException("This event need no participation request moderation");

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
}

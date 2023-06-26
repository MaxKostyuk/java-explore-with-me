package ru.practicum.mainsvc.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.event.enums.EventState;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;
import ru.practicum.mainsvc.request.enums.RequestStatus;
import ru.practicum.mainsvc.request.mapper.RequestMapper;
import ru.practicum.mainsvc.request.model.ParticipationRequest;
import ru.practicum.mainsvc.request.repository.RequestRepository;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<ParticipationRequestDto> getUsersRequests(Long userId) {
        userRepository.getUserById(userId);
        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userRepository.getUserById(userId);
        Event event = eventRepository.getEventById(eventId);
        if (requestRepository.requestAlreadyExists(userId, eventId))
            throw new DataIntegrityViolationException("Only one request from user for event is allowed");
        if (event.getInitiator().getId().equals(userId))
            throw new DataIntegrityViolationException("Initiator of event cannot request for participation");
        if (event.getState() != EventState.PUBLISHED)
            throw new DataIntegrityViolationException("Only published events are allowed for requests");
        if (event.getConfirmedRequests() >= event.getParticipantLimit() & !event.getParticipantLimit().equals(0))
            throw new DataIntegrityViolationException("There are no available slots for participation");
        ParticipationRequest request = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
        if (!event.getRequestModeration() | event.getParticipantLimit().equals(0))
            request.setStatus(RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.getUserById(userId);
        ParticipationRequest request = requestRepository.getRequestById(requestId);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}

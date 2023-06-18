package ru.practicum.mainsvc.request.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.mainsvc.exception.ElementNotFoundException;
import ru.practicum.mainsvc.request.model.ParticipationRequest;

public class RequestRepositoryCustomImpl implements RequestRepositoryCustom {
    private final RequestRepository repository;
    private static final String REQUEST_NOT_FOUND_TEMPLATE = "Request with id %d not found";

    public RequestRepositoryCustomImpl(@Lazy RequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ParticipationRequest getRequestById(Long requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new ElementNotFoundException(String.format(REQUEST_NOT_FOUND_TEMPLATE, requestId)));
    }

    @Override
    public boolean requestAlreadyExists(Long userId, Long eventId) {
        return repository.findByRequesterIdAndEventId(userId, eventId).isPresent();
    }
}

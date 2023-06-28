package ru.practicum.mainsvc.request.repository;

import ru.practicum.mainsvc.request.model.ParticipationRequest;

public interface RequestRepositoryCustom {

    ParticipationRequest getRequestById(Long requestId);

    boolean requestAlreadyExists(Long userId, Long eventId);
}

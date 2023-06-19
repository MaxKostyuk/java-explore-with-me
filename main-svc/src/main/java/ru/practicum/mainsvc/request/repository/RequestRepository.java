package ru.practicum.mainsvc.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long>, RequestRepositoryCustom {

    List<ParticipationRequest> findByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);


}

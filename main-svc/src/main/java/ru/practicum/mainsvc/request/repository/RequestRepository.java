package ru.practicum.mainsvc.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.request.model.ParticipationRequest;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long>, RequestRepositoryCustom {
}

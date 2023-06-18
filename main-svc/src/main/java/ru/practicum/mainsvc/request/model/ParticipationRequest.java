package ru.practicum.mainsvc.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.request.dto.RequestStatus;
import ru.practicum.mainsvc.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event", referencedColumnName = "id", nullable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id", nullable = false)
    private User requester;
    @Column(nullable = false)
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}

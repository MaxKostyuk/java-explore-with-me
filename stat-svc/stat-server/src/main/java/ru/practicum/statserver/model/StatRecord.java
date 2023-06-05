package ru.practicum.statserver.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistics", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String app;
    @Column(nullable = false, length = 20)
    private String uri;
    @Column(nullable = false, length = 20)
    private String ip;
    @Column(nullable = false)
    private LocalDateTime created;
}

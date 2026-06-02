package ru.practicum.mainservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "event_participants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant created;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Event event;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, referencedColumnName = "id", name = "requester")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}

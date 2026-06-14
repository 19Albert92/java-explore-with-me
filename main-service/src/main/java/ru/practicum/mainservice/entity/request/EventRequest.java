package ru.practicum.mainservice.entity.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime created;

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Event event;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, referencedColumnName = "id", name = "requester_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}

package ru.practicum.mainservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, referencedColumnName = "id", name = "initiator")
    private User user;

    private int confirmedRequests;

    @Column(nullable = false)
    private String annotation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Location location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    @Column(nullable = false)
    private String title;
}

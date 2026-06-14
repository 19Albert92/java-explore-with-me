package ru.practicum.mainservice.entity.event;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "initiator_id")
    private User initiator;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Embedded
    private Location location;

    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "created_on")
    private LocalDateTime createdAt;

    public boolean hasEventState(EventState findState) {
        return state.equals(findState);
    }

    public boolean isInitiator(Long userId) {
        return initiator.getId().equals(userId);
    }
}

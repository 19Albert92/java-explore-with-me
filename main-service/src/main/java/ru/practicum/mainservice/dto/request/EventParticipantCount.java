package ru.practicum.mainservice.dto.request;

public record EventParticipantCount(
        Long eventId,
        Long count
) {
}

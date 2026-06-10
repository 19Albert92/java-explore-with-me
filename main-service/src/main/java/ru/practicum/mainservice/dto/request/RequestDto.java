package ru.practicum.mainservice.dto.request;

import lombok.Builder;
import ru.practicum.mainservice.entity.request.ApplicationStatus;

import java.time.LocalDateTime;

@Builder
public record RequestDto(
        Long id,
        Long event,
        long requester,
        LocalDateTime created,
        ApplicationStatus status
) {
}

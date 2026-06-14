package ru.practicum.mainservice.dto.request;

import lombok.Builder;
import ru.practicum.mainservice.entity.request.ApplicationStatus;

@Builder
public record RequestDto(
        Long id,
        Long event,
        long requester,
        String created,
        ApplicationStatus status
) {
}

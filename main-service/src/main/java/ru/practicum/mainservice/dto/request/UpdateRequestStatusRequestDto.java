package ru.practicum.mainservice.dto.request;

import ru.practicum.mainservice.entity.request.ApplicationStatus;

import java.util.List;

public record UpdateRequestStatusRequestDto(
        List<Long> requestIds,
        ApplicationStatus status
) {
}

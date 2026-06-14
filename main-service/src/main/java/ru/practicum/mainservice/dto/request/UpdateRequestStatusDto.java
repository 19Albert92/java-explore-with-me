package ru.practicum.mainservice.dto.request;

import java.util.List;

public record UpdateRequestStatusDto(
        List<RequestDto> confirmedRequests,
        List<RequestDto> rejectedRequests
) {
}

package ru.practicum.mainservice.service.request;

import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusRequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> findRequestsByUserId(Long eventId, Long userId);

    UpdateRequestStatusDto updateRequestStatus(Long userId, Long eventId, UpdateRequestStatusRequestDto requestDto);

    List<RequestDto> findAllRequestByCurrentUser(Long userId);

    RequestDto saveRequestToEvent(Long userId, Long eventId);

    RequestDto cancelRequestToEvent(Long userId, Long requestId);
}

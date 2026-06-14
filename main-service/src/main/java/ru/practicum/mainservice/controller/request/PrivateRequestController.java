package ru.practicum.mainservice.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getCurrentUserApplications(
            @PathVariable Long userId
    ) {
        return requestService.findAllRequestByCurrentUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequestToCurrentApplications(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        return requestService.saveRequestToEvent(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequestToCurrentApplications(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return requestService.cancelRequestToEvent(userId, requestId);
    }
}

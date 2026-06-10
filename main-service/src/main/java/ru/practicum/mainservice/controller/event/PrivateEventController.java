package ru.practicum.mainservice.controller.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.NewEventDto;
import ru.practicum.mainservice.dto.event.UpdateEventDto;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusRequestDto;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    private final RequestService requestService;

    @GetMapping
    public List<EventShortDto> findEventsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return eventService.findAllByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto newEventDto
    ) {
        return eventService.save(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventById(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return eventService.findEventByUserId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventDto newEventDto
    ) {
        return eventService.update(eventId, userId, newEventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> findRequestsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return requestService.findRequestsByUserId(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public UpdateRequestStatusDto updateRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateRequestStatusRequestDto requestDto
    ) {
        return requestService.updateRequestStatus(userId, eventId, requestDto);
    }
}

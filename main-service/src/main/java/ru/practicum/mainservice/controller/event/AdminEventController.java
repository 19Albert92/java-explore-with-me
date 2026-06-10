package ru.practicum.mainservice.controller.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.QueryEventsDto;
import ru.practicum.mainservice.dto.event.UpdateEventAdminDto;
import ru.practicum.mainservice.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEventsByFilter(
           @Valid QueryEventsDto filterEventDto
    ) {
        return eventService.findAll(filterEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventById(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminDto eventAdminDto
    ) {
        return eventService.updateEventByEventId(eventId, eventAdminDto);
    }
}

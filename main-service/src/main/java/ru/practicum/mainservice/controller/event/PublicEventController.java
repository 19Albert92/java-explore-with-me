package ru.practicum.mainservice.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.event.EventFullDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.dto.event.FilterEventDto;
import ru.practicum.mainservice.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findEventsByFilter(
            @Valid FilterEventDto filterEventDto,
            HttpServletRequest request
    ) {
        return eventService.findAll(filterEventDto, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{id}")
    public EventFullDto findEventById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        return eventService.findById(id, request.getRemoteAddr(), request.getRequestURI());
    }
}

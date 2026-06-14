package ru.practicum.mainservice.service.event;

import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.entity.event.Event;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EventService {

    List<EventShortDto> findAll(FilterEventDto filterEventDto, String ip, String path);

    EventFullDto findById(Long id, String ip, String path);

    List<EventShortDto> findAllByUserId(Long userId, int from, int size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long eventId, Long userId, UpdateEventDto updateEventDto);

    EventFullDto findEventByUserId(Long eventId, Long userId);

    List<EventFullDto> findAll(QueryEventsDto filterEventDto);

    EventFullDto updateEventByEventId(Long eventId, UpdateEventAdminDto eventAdminDto);

    List<Event> findAllEvents(Set<Integer> eventIds);

    Map<String, Long> getViews(List<Event> events);

    List<EventShortDto> parseToEventShortDtoList(List<Event> events);
}

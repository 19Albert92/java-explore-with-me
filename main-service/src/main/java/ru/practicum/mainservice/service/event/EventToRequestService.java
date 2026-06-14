package ru.practicum.mainservice.service.event;

import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.exception.event.EventNotFoundException;

import java.util.List;
import java.util.Map;

public interface EventToRequestService {

    Event findEventByIdOrException(Long eventId) throws EventNotFoundException;

    Map<Long, Long> getParticipantsByEventIds(List<Long> eventIds);
}

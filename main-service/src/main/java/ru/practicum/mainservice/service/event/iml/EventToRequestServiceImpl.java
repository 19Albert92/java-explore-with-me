package ru.practicum.mainservice.service.event.iml;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.request.EventParticipantCount;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.exception.event.EventNotFoundException;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.service.event.EventToRequestService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventToRequestServiceImpl implements EventToRequestService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public Event findEventByIdOrException(Long eventId) throws EventNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(
                        "The required object was not found.",
                        String.format("Event with id=%d was not found", eventId)
                ));
    }

    @Override
    public Map<Long, Long> getParticipantsByEventIds(List<Long> eventIds) {
        return requestRepository.getParticipantsByEventIdsAndStatus(eventIds, ApplicationStatus.CONFIRMED).stream()
                .collect(Collectors.toMap(EventParticipantCount::eventId, EventParticipantCount::count));
    }
}

package ru.practicum.mainservice.service.event.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.event.*;
import ru.practicum.mainservice.entity.Category;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.event.EventNotFoundException;
import ru.practicum.mainservice.exception.event.FilterParamsNotValidException;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.query.EventQueryRepository;
import ru.practicum.mainservice.service.StateViewsService;
import ru.practicum.mainservice.service.category.CategoryService;
import ru.practicum.mainservice.service.event.EventService;
import ru.practicum.mainservice.service.event.EventToRequestService;
import ru.practicum.mainservice.service.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    private final EventToRequestService eventToRequestService;

    private final StateViewsService stateViewsService;

    private final EventQueryRepository eventQueryRepository;

    @Override
    public List<EventShortDto> findAllByUserId(Long userId, int from, int size) {

        userService.findByIdOrException(userId);

        PageRequest pageRequest = PageRequest.of(from, size);

        log.debug("Page request received: {} by userId: {}", pageRequest, userId);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event, 0, 0L))
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto save(Long userId, NewEventDto newEventDto) {

        User user = userService.findByIdOrException(userId);

        Category category = categoryService.findByIdOrException(newEventDto.category());

        Event event = EventMapper.toEvent(user, category, newEventDto);

        log.debug("Saving event {}", event);

        Event newEvent = eventRepository.save(event);

        return EventMapper.toEventFullDto(newEvent, 0, 0);
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, Long userId, UpdateEventDto updateEventDto) {

        Event event = findByIdAndUserIdOrException(eventId, userId);

        if (event.hasEventState(EventState.PUBLISHED)) {
            throw new ConflictException(
                    "For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed"
            );
        }

        Category category = null;

        if (updateEventDto.hasCategory() && !event.getCategory().getId().equals(updateEventDto.category())) {
            category = categoryService.findByIdOrException(updateEventDto.category());
        }

        Event updateEvent = EventMapper.toEvent(event, category, updateEventDto);

        updateEvent = eventRepository.save(updateEvent);

        return EventMapper.toEventFullDto(updateEvent, 0, 0);
    }

    @Override
    public EventFullDto findEventByUserId(Long eventId, Long userId) {

        Event event = findByIdAndUserIdOrException(eventId, userId);

        Map<String, Long> views = getViews(List.of(event));

        Map<Long, Long> requests = eventToRequestService.getParticipantsByEventIds(List.of(event.getId()));

        return EventMapper.toEventFullDto(
                event,
                requests.getOrDefault(event.getId(), 0L),
                views.getOrDefault("/events/" + event.getId(), 0L)
        );
    }

    @Override
    public List<EventFullDto> findAll(QueryEventsDto filterEventDto) {

        List<Event> events = eventQueryRepository.queryEventsByAdminFilter(filterEventDto);

        Map<String, Long> views = getViews(events);

        List<Long> ids = events.stream().map(Event::getId).toList();

        Map<Long, Long> requests = eventToRequestService.getParticipantsByEventIds(ids);

        return events.stream()
                .map(event ->
                        EventMapper.toEventFullDto(
                                event,
                                requests.getOrDefault(event.getId(), 0L),
                                views.getOrDefault("/events/" + event.getId(), 0L)
                        )
                ).toList();
    }

    @Override
    @Transactional
    public EventFullDto updateEventByEventId(Long eventId, UpdateEventAdminDto eventAdminDto) {

        Event event = eventToRequestService.findEventByIdOrException(eventId);

        if (!event.hasEventState(EventState.PENDING)) {
            throw new ConflictException(
                    "For the requested operation the conditions are not met.",
                    "Cannot publish the event because it's not in the right state: PUBLISHED"
            );
        }

        Category category = null;

        if (eventAdminDto.hasCategory() && !eventAdminDto.category().equals(event.getCategory().getId())) {
            category = categoryService.findByIdOrException(eventAdminDto.category());
        }

        Event newEvent = EventMapper.toEvent(event, category, eventAdminDto);

        newEvent = eventRepository.save(newEvent);

        log.warn("Updating event {}", newEvent);

        return EventMapper.toEventFullDto(newEvent, 0, 0);
    }

    @Override
    public List<Event> findAllEvents(Set<Integer> eventIds) {
        return eventRepository.findAllByIdIn(eventIds);
    }

    @Override
    @Transactional
    public List<EventShortDto> findAll(FilterEventDto filterEventDto, String ip, String path) {

        stateViewsService.saveState(ip, path);

        if (filterEventDto.rangeStart() != null && filterEventDto.rangeEnd() != null) {
            if (filterEventDto.rangeEnd().isBefore(filterEventDto.rangeStart())) {
                throw new FilterParamsNotValidException(
                        "Incorrectly made request.",
                        "Incorrectly filter params"
                );
            }
        }

        return parseToEventShortDtoList(eventQueryRepository.findEventsByFilter(filterEventDto));
    }

    @Override
    @Transactional
    public EventFullDto findById(Long id, String ip, String path) {

        stateViewsService.saveState(ip, path);

        Event event = eventToRequestService.findEventByIdOrException(id);

        if (!event.hasEventState(EventState.PUBLISHED)) {
            log.error("Cannot find published event with id {}", id);
            throw new EventNotFoundException(
                    "The required object was not found.",
                    String.format("Event with id=%d was not found", id)
            );
        }

        Map<String, Long> views = getViews(List.of(event));

        Map<Long, Long> requests = eventToRequestService.getParticipantsByEventIds(List.of(event.getId()));

        return EventMapper.toEventFullDto(
                event,
                requests.getOrDefault(event.getId(), 0L),
                views.getOrDefault("/events/" + event.getId(), 0L) + 1
        );
    }

    @Override
    public List<EventShortDto> parseToEventShortDtoList(List<Event> events) {

        Map<String, Long> views = getViews(events);

        List<Long> ids = events.stream().map(Event::getId).toList();

        Map<Long, Long> requests = eventToRequestService.getParticipantsByEventIds(ids);

        return events.stream()
                .map(e -> EventMapper.toEventShortDto(
                        e,
                        requests.getOrDefault(e.getId(), 0L),
                        views.getOrDefault("/events/" + e.getId(), 0L)
                ))
                .toList();
    }

    public Map<String, Long> getViews(List<Event> events) {
        LocalDateTime start = events.stream()
                .map(Event::getCreatedAt)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        List<String> uris = events.stream().map(e -> "/events/" + e.getId()).toList();

        return stateViewsService.getStaticsByFilter(start, uris, true);
    }

    private Event findByIdAndUserIdOrException(Long eventId, Long userId) throws EventNotFoundException {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(
                        "The required object was not found.",
                        String.format("Event with id=%d was not found", eventId)
                ));
    }
}

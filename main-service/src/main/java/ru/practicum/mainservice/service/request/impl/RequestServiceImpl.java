package ru.practicum.mainservice.service.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusDto;
import ru.practicum.mainservice.dto.request.UpdateRequestStatusRequestDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.EventRequest;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.event.EventNotFoundException;
import ru.practicum.mainservice.mapper.RequestMapping;
import ru.practicum.mainservice.repository.RequestRepository;
import ru.practicum.mainservice.service.event.EventToRequestService;
import ru.practicum.mainservice.service.request.RequestService;
import ru.practicum.mainservice.service.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventToRequestService eventToRequestService;

    @Override
    public List<RequestDto> findRequestsByUserId(Long eventId, Long userId) {

        Event event = eventToRequestService.findEventByIdOrException(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "The user is not the creator of the event"
            );
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapping::mapToRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public UpdateRequestStatusDto updateRequestStatus(Long userId, Long eventId, UpdateRequestStatusRequestDto requestDto) {

        Event event = eventToRequestService.findEventByIdOrException(eventId);

        long remainingQuantity = remainingNumberOfApplications(event);

        requestRepository.updateRequestsStatus(requestDto.requestIds(), requestDto.status(), eventId);

        if (event.getParticipantLimit() > 0 && remainingQuantity == event.getParticipantLimit()) {
            requestRepository.resetQueriesOther(eventId);
        }

        Map<ApplicationStatus, List<EventRequest>> request = requestRepository.findAllByEventId(eventId).stream()
                .collect(Collectors.groupingBy(EventRequest::getStatus));

        return new UpdateRequestStatusDto(
                request.getOrDefault(ApplicationStatus.CONFIRMED, List.of()).stream().map(RequestMapping::mapToRequestDto).toList(),
                request.getOrDefault(ApplicationStatus.REJECTED, List.of()).stream().map(RequestMapping::mapToRequestDto).toList()
        );
    }

    @Override
    public List<RequestDto> findAllRequestByCurrentUser(Long userId) {

        List<EventRequest> requests = requestRepository.findAllByUserId(userId);

        return requests.stream()
                .map(RequestMapping::mapToRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestDto saveRequestToEvent(Long userId, Long eventId) {

        User requester = userService.findByIdOrException(userId);

        Event event = eventToRequestService.findEventByIdOrException(eventId);

        if (event.hasEventState(EventState.PENDING)) {
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "The event is no longer in pending mode"
            );
        }

        checkInitiator(event, userId);

        remainingNumberOfApplications(event);

        ApplicationStatus requestStatus = ApplicationStatus.PENDING;

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            requestStatus = ApplicationStatus.CONFIRMED;
        }

        if (requestRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "Request with userId and eventId already exists"
            );
        }

        EventRequest eventRequest = RequestMapping.mapToEventRequest(requester, event, requestStatus);

        eventRequest = requestRepository.save(eventRequest);

        return RequestMapping.mapToRequestDto(eventRequest);
    }

    private void checkInitiator(Event event, Long userId) {
        if (event.isInitiator(userId)) {
            throw new ConflictException(
                    "Integrity constraint has been violated.",
                    "You can't add a request to your event"
            );
        }
    }

    private long remainingNumberOfApplications(Event event) {

        if (event.getParticipantLimit() == 0) {
            log.debug("Unlimited Entry for Everyone");
            return 0;
        }

        Map<Long, Long> requests = eventToRequestService.getParticipantsByEventIds(List.of(event.getId()));

        long count = requests.getOrDefault(event.getId(), 0L);

        if (count >= event.getParticipantLimit()) {
            throw new ConflictException(
                    "For the requested operation the conditions are not met.",
                    "The participant limit has been reached"
            );
        }

        return count - 1;
    }

    @Override
    public RequestDto cancelRequestToEvent(Long userId, Long requestId) {

        userService.findByIdOrException(userId);

        EventRequest request = findByIdOrException(requestId);

        EventRequest updateRequest = RequestMapping.mapToEventRequestWithCancelStatus(request);

        request = requestRepository.save(updateRequest);

        return RequestMapping.mapToRequestDto(request);
    }

    public EventRequest findByIdOrException(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new EventNotFoundException(
                        "The required object was not found.",
                        String.format("Request with id=%d was not found", requestId)
                ));
    }
}

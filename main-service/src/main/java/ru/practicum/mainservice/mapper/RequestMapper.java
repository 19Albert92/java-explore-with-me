package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.entity.User;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.entity.request.ApplicationStatus;
import ru.practicum.mainservice.entity.request.EventRequest;

import java.time.LocalDateTime;

import static shared.UtilConstant.FORMATTER;

@UtilityClass
public class RequestMapper {

    public static EventRequest mapToEventRequest(User requester, Event event, ApplicationStatus status) {
        return EventRequest.builder()
                .user(requester)
                .event(event)
                .status(status)
                .created(LocalDateTime.now())
                .build();
    }

    public static RequestDto mapToRequestDto(EventRequest eventRequest) {
        return RequestDto.builder()
                .id(eventRequest.getId())
                .status(eventRequest.getStatus())
                .event(eventRequest.getEvent().getId())
                .requester(eventRequest.getUser().getId())
                .created(eventRequest.getCreated().format(FORMATTER))
                .build();
    }

    public static EventRequest mapToEventRequestWithCancelStatus(EventRequest eventRequest) {
        eventRequest.setStatus(ApplicationStatus.CANCELED);
        return eventRequest;
    }
}

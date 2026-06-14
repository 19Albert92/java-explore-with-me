package ru.practicum.mainservice.dto.event;

import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.entity.event.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static shared.UtilConstant.DATE_TIME_FORMAT;

public record QueryEventsDto(
        List<Long> users,
        List<EventState> states,
        List<Long> categories,
        @DateTimeFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime rangeStart,
        @DateTimeFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime rangeEnd,
        Integer from,
        Integer size
) {
}

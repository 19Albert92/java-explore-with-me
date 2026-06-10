package ru.practicum.mainservice.dto.event;

import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.entity.event.EventSortView;

import java.time.LocalDateTime;
import java.util.List;

import static shared.UtilConstant.DATE_TIME_FORMAT;

public record FilterEventDto(
        String text,
        List<Long> categories,
        Boolean paid,
        @DateTimeFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime rangeStart,
        @DateTimeFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime rangeEnd,
        Boolean onlyAvailable,
        EventSortView sort,
        Integer from,
        Integer size
) {
}

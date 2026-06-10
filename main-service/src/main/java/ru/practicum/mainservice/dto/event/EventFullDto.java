package ru.practicum.mainservice.dto.event;

import lombok.Builder;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.locaion.LocationDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.entity.event.EventState;

@Builder
public record EventFullDto(
        Long id,
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        LocationDto location,
        String createdOn,
        String eventDate,
        UserShortDto initiator,
        boolean paid,
        int participantLimit,
        String publishedOn,
        boolean requestModeration,
        EventState state,
        String title,
        String description,
        long views
) {
}

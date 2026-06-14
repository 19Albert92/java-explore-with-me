package ru.practicum.mainservice.dto.event;

import lombok.Builder;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.user.UserShortDto;
import ru.practicum.mainservice.entity.event.EventState;
import ru.practicum.mainservice.entity.event.Location;

@Builder
public record EventFullDto(
        Long id,
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        Location location,
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

package ru.practicum.mainservice.dto.event;

import lombok.Builder;
import ru.practicum.mainservice.dto.category.CategoryDto;
import ru.practicum.mainservice.dto.user.UserShortDto;

@Builder
public record EventShortDto(
        Long id,
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        String eventDate,
        UserShortDto initiator,
        boolean paid,
        String title,
        long views
) {
}

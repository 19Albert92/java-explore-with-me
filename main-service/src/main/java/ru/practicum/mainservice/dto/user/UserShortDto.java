package ru.practicum.mainservice.dto.user;

import lombok.Builder;

@Builder
public record UserShortDto(
        Long id,
        String name
) {
}

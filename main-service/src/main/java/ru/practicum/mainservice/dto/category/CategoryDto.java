package ru.practicum.mainservice.dto.category;

import lombok.Builder;

@Builder
public record CategoryDto(
        Long id,
        String name
) {
}

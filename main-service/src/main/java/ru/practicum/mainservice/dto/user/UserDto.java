package ru.practicum.mainservice.dto.user;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String name
) {
}

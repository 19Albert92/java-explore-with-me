package ru.practicum.mainservice.dto.locaion;

import lombok.Builder;

@Builder
public record LocationDto(
        float lat,
        float lon
) {
}

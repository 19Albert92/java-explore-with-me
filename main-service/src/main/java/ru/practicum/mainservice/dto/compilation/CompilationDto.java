package ru.practicum.mainservice.dto.compilation;

import lombok.Builder;
import ru.practicum.mainservice.dto.event.EventShortDto;

import java.util.List;

@Builder
public record CompilationDto(
        Long id,
        List<EventShortDto> events,
        boolean pinned,
        String title
) {
}

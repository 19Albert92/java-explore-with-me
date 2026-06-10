package ru.practicum.mainservice.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.event.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.title())
                .events(events)
                .pinned(newCompilationDto.pinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.isPinned())
                .build();
    }

    public static Compilation toCompilation(
            Compilation compilation, UpdateCompilationDto compilationDto, List<Event> events) {

        if (compilationDto.hasPinned()) {
            compilation.setPinned(compilationDto.pinned());
        }

        if (compilationDto.hasTitle()) {
            compilation.setTitle(compilationDto.title());
        }

        if (compilationDto.hasEvents()) {
            compilation.setEvents(events);
        }

        return compilation;
    }
}

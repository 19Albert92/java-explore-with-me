package ru.practicum.mainservice.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.event.Event;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CompilationMapperTest {

    private static final Long ID = 1L;
    private static final String TITLE = "Some title";
    private static final Set<Integer> EVENT_IDS = Set.of(1, 2);

    private final List<Event> events = List.of(
            Event.builder().id(1L).build(),
            Event.builder().id(2L).build()
    );

    private final Compilation compilation = Compilation.builder().id(ID).title(TITLE).build();


    @Test
    void mapToCompilation() {

        NewCompilationDto compilationDto = NewCompilationDto.builder()
                .title(TITLE)
                .events(EVENT_IDS)
                .build();

        Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);

        assertThat(compilation)
                .isNotNull()
                .isInstanceOf(Compilation.class)
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("events", events);
    }

    @Test
    void mapToCompilationDto() {

        List<EventShortDto> eventShortDtos = List.of(
                EventShortDto.builder().id(1L).build(),
                EventShortDto.builder().id(2L).build(),
                EventShortDto.builder().id(3L).build()
        );

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation, eventShortDtos);

        assertThat(compilationDto)
                .isNotNull()
                .isInstanceOf(CompilationDto.class)
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("events", eventShortDtos);
    }

    @Test
    void mapToCompilationUpdated() {

        UpdateCompilationDto updateCompilationDto = UpdateCompilationDto.builder().events(EVENT_IDS).build();

        Compilation updateCompilations = CompilationMapper.toCompilation(compilation, updateCompilationDto, events);

        assertThat(updateCompilations)
                .isNotNull()
                .isInstanceOf(Compilation.class)
                .hasFieldOrPropertyWithValue("id", ID)
                .hasFieldOrPropertyWithValue("title", TITLE)
                .hasFieldOrPropertyWithValue("events", events);
    }
}
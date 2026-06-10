package ru.practicum.mainservice.service.compilation.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.mainservice.dto.event.EventShortDto;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.entity.event.Event;
import ru.practicum.mainservice.exception.compilation.CompilationNotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.service.compilation.CompilationService;
import ru.practicum.mainservice.service.event.EventService;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto compilationDto) {

        List<Event> events = eventService.findAllEvents(compilationDto.events());

        Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);

        compilation = compilationRepository.save(compilation);

        List<EventShortDto> eventsDto = eventService.parseToEventShortDtoList(events);

        log.debug("save compilation {}", compilation);

        return CompilationMapper.toCompilationDto(compilation, eventsDto);
    }

    @Override
    @Transactional
    public void delete(Long compId) {

        Compilation compilation = findByIdOrException(compId);

        compilationRepository.delete(compilation);

        log.info("Delete compilation with id {}", compilation.getId());
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationDto compilationDto) {

        Compilation compilation = findByIdOrException(compId);

        List<Event> events = eventService.findAllEvents(compilationDto.events());

        Compilation updateCompilation = CompilationMapper.toCompilation(compilation, compilationDto, events);

        compilationRepository.save(updateCompilation);

        List<EventShortDto> eventsDto = eventService.parseToEventShortDtoList(events);

        log.debug("update compilation {}", updateCompilation);


        return CompilationMapper.toCompilationDto(compilation, eventsDto);
    }

    @Override
    public Compilation findByIdOrException(Long compId) throws CompilationNotFoundException {
        log.info("Find compilation with id {}", compId);
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(
                        "The required object was not found.",
                        String.format("Compilation with id=%d was not found", compId)
                ));
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {

        Specification<Compilation> specification = ((root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (pinned != null) {
                predicates.add(builder.equal(root.get("pinned"), pinned));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });

        return compilationRepository.findAll(specification, PageRequest.of(from, size)).stream()
                .map(compilation ->
                        CompilationMapper.toCompilationDto(
                                compilation,
                                eventService.parseToEventShortDtoList(compilation.getEvents())
                        ))
                .toList();
    }

    @Override
    public CompilationDto findById(Long compId) {

        Compilation compilation = findByIdOrException(compId);

        return CompilationMapper.toCompilationDto(
                compilation,
                eventService.parseToEventShortDtoList(compilation.getEvents())
        );
    }
}

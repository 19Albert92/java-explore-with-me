package ru.practicum.mainservice.service.compilation;

import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationDto;
import ru.practicum.mainservice.entity.Compilation;
import ru.practicum.mainservice.exception.compilation.CompilationNotFoundException;

import java.util.List;

public interface CompilationService {

    CompilationDto save(NewCompilationDto compilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationDto compilationDto);

    Compilation findByIdOrException(Long compId) throws CompilationNotFoundException;

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto findById(Long compId);
}

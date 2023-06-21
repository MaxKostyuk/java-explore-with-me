package ru.practicum.mainsvc.compilation;

import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilation);

    void deleteCompilation(Long compilationId);

    CompilationDto updateCompilation(Long compilationId, NewCompilationDto updatedCompilation);

    List<CompilationDto> search(Boolean pinned, int from, int size);

    CompilationDto getById(int compilationId);
}

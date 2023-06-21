package ru.practicum.mainsvc.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilation) {
        return null;
    }

    @Override
    public void deleteCompilation(Long compilationId) {

    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, NewCompilationDto updatedCompilation) {
        return null;
    }

    @Override
    public List<CompilationDto> search(Boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getById(int compilationId) {
        return null;
    }
}

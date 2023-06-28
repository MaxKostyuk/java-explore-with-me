package ru.practicum.mainsvc.compilation.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.mainsvc.compilation.model.Compilation;
import ru.practicum.mainsvc.exception.ElementNotFoundException;

public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {

    private final CompilationRepository repository;

    private static final String COMPILATION_NOT_FOUND_TEMPLATE = "Compilation with id %d not found";

    public CompilationRepositoryCustomImpl(@Lazy CompilationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Compilation getCompilationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format(COMPILATION_NOT_FOUND_TEMPLATE, id)));
    }
}

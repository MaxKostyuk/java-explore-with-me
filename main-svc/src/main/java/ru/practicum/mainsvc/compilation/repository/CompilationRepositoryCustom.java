package ru.practicum.mainsvc.compilation.repository;

import ru.practicum.mainsvc.compilation.model.Compilation;

public interface CompilationRepositoryCustom {

    Compilation getCompilationById(Long id);
}

package ru.practicum.mainsvc.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainsvc.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {
}

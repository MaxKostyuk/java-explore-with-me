package ru.practicum.mainsvc.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {

    @Query("select c from Compilation c where (:pinned is null or c.pinned = :pinned)")
    List<Compilation> search(Boolean pinned, Pageable page);
}

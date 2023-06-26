package ru.practicum.mainsvc.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainsvc.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilation) {
        log.info("AdminCompilationController - addCompilation - newCompilation = {}", newCompilation);
        return service.addCompilation(newCompilation);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compilationId) {
        log.info("AdminCompilationController - deleteCompilation - compilationId = {}", compilationId);
        service.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationRequest updatedCompilation,
                                            @PathVariable @Positive Long compilationId) {
        return service.updateCompilation(compilationId, updatedCompilation);
    }
}

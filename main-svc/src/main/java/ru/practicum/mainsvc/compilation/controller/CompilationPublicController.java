package ru.practicum.mainsvc.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.compilation.service.CompilationService;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> searchCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        return service.search(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getById(@PathVariable @Positive Long compilationId) {
        return service.getById(compilationId);
    }
}

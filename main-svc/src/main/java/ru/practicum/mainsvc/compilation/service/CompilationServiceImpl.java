package ru.practicum.mainsvc.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainsvc.compilation.mapper.CompilationMapper;
import ru.practicum.mainsvc.compilation.model.Compilation;
import ru.practicum.mainsvc.compilation.repository.CompilationRepository;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.exception.ElementNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilation) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilation);
        List<Event> events = new ArrayList<>();
        if (newCompilation.getEvents() != null) {
            events = eventRepository.findAllById(newCompilation.getEvents());
            if (events.size() != newCompilation.getEvents().size())
                throw new ElementNotFoundException("You are adding not existing event. Check event IDs");
        }
        compilation.setEvents(events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.getCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest updatedCompilation) {
        Compilation compilation = compilationRepository.getCompilationById(compilationId);
        if (updatedCompilation.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updatedCompilation.getEvents());
            if (events.size() != updatedCompilation.getEvents().size())
                throw new ElementNotFoundException("You are adding not existing event. Check event IDs");
            compilation.setEvents(events);
        }
        if (updatedCompilation.getPinned() != null)
            compilation.setPinned(updatedCompilation.getPinned());
        if (updatedCompilation.getTitle() != null)
            compilation.setTitle(updatedCompilation.getTitle());
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> search(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        return compilationRepository.search(pinned, page).stream()
                .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.getCompilationById(compilationId));
    }
}

package ru.practicum.mainsvc.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.event.dto.EventFullDto;
import ru.practicum.mainsvc.event.dto.EventState;
import ru.practicum.mainsvc.event.dto.NewEventDto;
import ru.practicum.mainsvc.event.mapper.EventMapper;
import ru.practicum.mainsvc.event.mapper.LocationMapper;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public EventFullDto addEvent(NewEventDto newEvent, Long userId) {
        User user = userRepository.getUserById(userId);
        Category category = categoryRepository.getCategoryById(newEvent.getCategory());
        Event event = EventMapper.toEvent(newEvent);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(LocationMapper.toLocation(newEvent.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}

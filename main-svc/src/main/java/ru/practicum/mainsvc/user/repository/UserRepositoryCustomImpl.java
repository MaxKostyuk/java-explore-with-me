package ru.practicum.mainsvc.user.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.mainsvc.exception.ElementNotFoundException;
import ru.practicum.mainsvc.user.model.User;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final UserRepository repository;
    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    public UserRepositoryCustomImpl(@Lazy UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, userId)));
    }
}

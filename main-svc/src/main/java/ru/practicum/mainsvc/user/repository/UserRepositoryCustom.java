package ru.practicum.mainsvc.user.repository;

import ru.practicum.mainsvc.user.model.User;

public interface UserRepositoryCustom {

    User getUserById(Long userId);
}

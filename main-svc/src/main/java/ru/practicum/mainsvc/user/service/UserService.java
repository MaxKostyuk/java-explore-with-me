package ru.practicum.mainsvc.user.service;

import ru.practicum.mainsvc.user.dto.NewUserRequest;
import ru.practicum.mainsvc.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto addUser(NewUserRequest userRequest);

    void deleteUser(Long userId);
}

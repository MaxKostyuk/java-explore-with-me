package ru.practicum.mainsvc.user.mapper;

import ru.practicum.mainsvc.user.dto.NewUserRequest;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.model.User;

public class UserMapper {

    public static User toUser(NewUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

package ru.practicum.mainsvc.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.user.dto.NewUserRequest;
import ru.practicum.mainsvc.user.dto.UserDto;
import ru.practicum.mainsvc.user.mapper.UserMapper;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;


    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<User> userList;
        if (ids == null) {
            userList = repository.findAll(page).getContent();
        } else {
            userList = repository.findAllByIdIn(ids, page);
        }
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(NewUserRequest userRequest) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userRequest)));
    }

    @Override
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }
}

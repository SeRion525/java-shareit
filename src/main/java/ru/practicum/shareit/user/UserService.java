package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto saveUser(UserCreateDto userDto);

    UserDto updateUser(UserUpdateDto userDto);

    void deleteUser(Long userId);
}

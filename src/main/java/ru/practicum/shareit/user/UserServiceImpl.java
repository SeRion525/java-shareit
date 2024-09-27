package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String NOT_FOUND_USER = "Не найден пользователь с ID = ";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAll().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.getById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
    }

    @Override
    public UserDto saveUser(UserCreateDto userDto) {
        if (isUsedEmail(userDto.getEmail())) {
            throw new DuplicatedDataException("Данный email уже используется");
        }

        User user = userMapper.toUser(userDto);
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userDto) {
        User updatedUser = userRepository.getById(userDto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userDto.getId()));

        if (userDto.getEmail() != null && isUsedEmail(userDto.getEmail())) {
            throw new DuplicatedDataException("Данный email уже используется");
        }

        updatedUser = userRepository.update(userMapper.updateUserFields(updatedUser, userDto));
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    private boolean isUsedEmail(String email) {
        return userRepository.getEmails().contains(email);
    }
}

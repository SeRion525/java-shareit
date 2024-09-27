package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserCreateDto userDto);

    default User updateUserFields(User user, UserUpdateDto userDto) {
        if (userDto.getName() != null && !userDto.getName().equals(user.getName())) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }

        return user;
    }
}

package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserShortDto;

@Data
public class ItemDto {
    private Long id;
    private UserShortDto owner;
    private String name;
    private String description;
    private Boolean available;


}

package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto getItem(Long itemId);

    ItemDto addNewItem(Long ownerId, ItemCreateDto itemDto);

    ItemDto updateItem(Long ownerId, ItemUpdateDto itemDto);

    List<ItemDto> searchItems(String text);
}

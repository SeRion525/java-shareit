package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    List<ItemInfoDto> getItemsByOwnerId(Long ownerId);

    ItemInfoDto getItem(Long itemId);

    ItemDto addNewItem(Long ownerId, ItemCreateDto itemDto);

    ItemDto updateItem(Long ownerId, ItemUpdateDto itemDto);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(CommentCreateDto commentCreateDto);
}

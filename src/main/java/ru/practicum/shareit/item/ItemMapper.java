package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Item toItem(ItemCreateDto itemCreateDto);

    default Item updateItemFields(Item item, ItemUpdateDto itemUpdateDto) {
        if (itemUpdateDto.getName() != null && !itemUpdateDto.getName().equals(item.getName())) {
            item.setName(itemUpdateDto.getName());
        }

        if (itemUpdateDto.getDescription() != null && !itemUpdateDto.getDescription().equals(item.getDescription())) {
            item.setDescription(itemUpdateDto.getDescription());
        }

        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }

        return item;
    }
}

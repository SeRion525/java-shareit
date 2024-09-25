package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static ru.practicum.shareit.user.UserServiceImpl.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    public static final String NOT_FOUND_ITEM = "Не найдена вещь с ID = ";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));

        return itemRepository.getAllByUserId(user.getId()).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemRepository.getById(itemId)
                .map(itemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + itemId));
    }

    @Override
    public ItemDto addNewItem(Long ownerId, ItemCreateDto itemDto) {
        User owner = userRepository.getById(ownerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + ownerId));

        Item item = itemMapper.toItem(itemDto);
        item.setOwnerId(owner.getId());
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long ownerId, ItemUpdateDto itemDto) {
        User owner = userRepository.getById(ownerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + ownerId));
        Item updatedItem = itemRepository.getById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + itemDto.getId()));

        if (!owner.getId().equals(updatedItem.getOwnerId())) {
            throw new AccessDeniedException("Вещь с ID = " + itemDto.getId() + " не принадлежит пользователю с ID = " + ownerId);
        }

        updatedItem = itemRepository.update(itemMapper.updateItemFields(updatedItem, itemDto));
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }
}

package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.user.UserServiceImpl.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    public static final String NOT_FOUND_ITEM = "Не найдена вещь с ID = ";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingDto> getItemsByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(NOT_FOUND_USER + ownerId);
        }

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        return items.stream()
                .map(item -> {
                    Booking last = bookingRepository.findLastByItemId(item.getId()).orElse(null);
                    Booking next = bookingRepository.findNextByItemId(item.getId()).orElse(null);
                    return itemMapper.toItemWithBookingDto(item, last, next);
                })
                .toList();
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + itemId));
    }

    @Override
    @Transactional
    public ItemDto addNewItem(Long ownerId, ItemCreateDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + ownerId));

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, ItemUpdateDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + ownerId));
        Item updatedItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + itemDto.getId()));

        if (!owner.getId().equals(updatedItem.getOwner().getId())) {
            throw new AccessDeniedException("Вещь с ID = " + itemDto.getId() + " не принадлежит пользователю с ID = " + ownerId);
        }

        updatedItem = itemRepository.save(itemMapper.updateItemFields(updatedItem, itemDto));
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemRepository.findItemsByText(text).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }
}

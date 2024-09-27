package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAllByUserId(long userId);

    Optional<Item> getById(long itemId);

    Item save(Item item);

    Item update(Item item);

    List<Item> searchItems(String text);
}

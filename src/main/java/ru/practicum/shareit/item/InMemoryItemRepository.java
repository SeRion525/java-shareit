package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Set<Long>> usersAndItems = new HashMap<>();
    private long itemCounter = 0;

    @Override
    public List<Item> getAllByUserId(long userId) {
        Set<Long> userItems = usersAndItems.get(userId);
        if (userItems != null) {
            return userItems.stream()
                    .map(items::get)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Item> getById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item save(Item item) {
        item.setId(generateId());
        items.putIfAbsent(item.getId(), item);
        if (usersAndItems.containsKey(item.getOwnerId())) {
            usersAndItems.computeIfPresent(item.getOwnerId(), (key, value) -> value).add(item.getId());
        } else {
            usersAndItems.computeIfAbsent(item.getOwnerId(), key -> new HashSet<>()).add(item.getId());
        }

        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> containsIgnoreCase(item.getName(), text) || containsIgnoreCase(item.getDescription(), text))
                .toList();
    }

    private long generateId() {
        return ++itemCounter;
    }

    private boolean containsIgnoreCase(String target, String seq) {
        if (target == null || seq == null) {
            return false;
        }

        target = target.toLowerCase();
        seq = seq.toLowerCase();
        return target.contains(seq);
    }
}

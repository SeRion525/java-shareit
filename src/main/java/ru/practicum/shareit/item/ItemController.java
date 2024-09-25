package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestBody @Valid ItemCreateDto itemDto) {
        return itemService.addNewItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @PathVariable Long itemId,
                              @RequestBody @Valid ItemUpdateDto itemDto) {

        itemDto.setId(itemId);
        return itemService.updateItem(ownerId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemService.searchItems(text);
    }
}

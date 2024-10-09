package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

import static ru.practicum.shareit.util.Headers.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemInfoDto getItem(@PathVariable Long itemId) {
        log.info("Get item with id {}", itemId);
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsByOwnerId(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Get items by owner id {}", ownerId);
        return itemService.getItemsByOwnerId(ownerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewItem(@RequestHeader(X_SHARER_USER_ID) Long ownerId, @RequestBody @Valid ItemCreateDto itemDto) {
        log.info("Add new item {}", itemDto);
        ItemDto item = itemService.addNewItem(ownerId, itemDto);
        log.info("Added new item {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                              @PathVariable Long itemId,
                              @RequestBody @Valid ItemUpdateDto itemDto) {

        itemDto.setId(itemId);
        log.info("Update item {}", itemDto);
        ItemDto item = itemService.updateItem(ownerId, itemDto);
        log.info("Updated item {}", item);
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        log.info("Search items with text {}", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentCreateDto commentCreateDto) {

        commentCreateDto.setAuthorId(userId);
        commentCreateDto.setItemId(itemId);
        log.info("Add comment {}", commentCreateDto);
        CommentDto comment = itemService.addComment(commentCreateDto);
        log.info("Added comment {}", comment);
        return comment;
    }
}

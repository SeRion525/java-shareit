package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserServiceImpl.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    public static final String NOT_FOUND_ITEM = "Не найдена вещь с ID = ";
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemInfoDto> getItemsByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(NOT_FOUND_USER + ownerId);
        }

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> itemIds = items.stream().map(Item::getId).toList();
        LocalDateTime current = LocalDateTime.now();

        List<Booking> lastBookings = bookingRepository.findLastByItemIds(itemIds, current);
        List<Booking> nextBookings = bookingRepository.findNextByItemIds(itemIds, current);
        List<Comment> allComments = commentRepository.findAllByItemIdIn(itemIds);

        Map<Long, Booking> lastBookingsByItemId = listToMapByItemId(lastBookings);
        Map<Long, Booking> nextBookingsByItemId = listToMapByItemId(nextBookings);

        Map<Long, List<Comment>> commentsByItemId;
        if (allComments != null && !allComments.isEmpty()) {
            commentsByItemId = allComments.stream()
                    .collect(Collectors.groupingBy(c -> c.getItem().getId(), Collectors.toList()));
        } else {
            commentsByItemId = new HashMap<>();
        }

        return items.stream()
                .map(item -> {
                    Booking last = lastBookingsByItemId.get(item.getId());
                    Booking next = nextBookingsByItemId.get(item.getId());
                    List<Comment> comments = commentsByItemId.get(item.getId());
                    return itemMapper.toItemInfoDto(item, last, next, comments);
                })
                .toList();
    }

    @Override
    public ItemInfoDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + itemId));

        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return itemMapper.toItemInfoDto(item, null, null, comments);
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

    @Override
    @Transactional
    public CommentDto addComment(CommentCreateDto commentCreateDto) {
        User author = userRepository.findById(commentCreateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + commentCreateDto.getAuthorId()));
        Item item = itemRepository.findById(commentCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + commentCreateDto.getItemId()));

        if (!bookingRepository.isMakeBooking(author.getId(), item.getId(), LocalDateTime.now())) {
            throw new ValidationException("Пользователь ID = " + author.getId() +
                    " не бронировал вещь с ID = " + item.getId());
        }

        Comment comment = commentMapper.toComment(commentCreateDto);
        comment.setAuthor(author);
        comment.setItem(item);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    private Map<Long, Booking> listToMapByItemId(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return new HashMap<>();
        }

        Map<Long, Booking> bookingsByItemId = new HashMap<>();
        bookings.forEach(b -> bookingsByItemId.put(b.getItem().getId(), b));
        return bookingsByItemId;
    }
}

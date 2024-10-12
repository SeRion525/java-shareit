package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentShortDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "owner", source = "owner")
    ItemDto toItemDto(Item item);

    @Mapping(target = "authorName", source = "author.name")
    CommentShortDto toCommentShortDto(Comment comment);

    @Mapping(target = "id", source = "item.id")
    ItemInfoDto toItemInfoDto(Item item,
                              Booking lastBooking, Booking nextBooking,
                              List<Comment> comments);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    ItemInfoDto.BookingDto toBookingDto(Booking booking);

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

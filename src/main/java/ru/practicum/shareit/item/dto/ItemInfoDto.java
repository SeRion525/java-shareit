package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.comment.dto.CommentShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentShortDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;
    }
}

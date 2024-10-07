package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserShortDto;

@Data
public class BookingDto {
    private Long id;
    private ItemShortDto item;
    private UserShortDto booker;
    private String status;
    private String start;
    private String end;

    @Data
    public static class ItemShortDto {
        private Long id;
        private String name;
    }
}

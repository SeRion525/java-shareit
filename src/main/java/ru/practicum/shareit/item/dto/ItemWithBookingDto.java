package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    @Data
    public static class BookingDto {
        private Long id;
        private Long bookerId;
    }
}

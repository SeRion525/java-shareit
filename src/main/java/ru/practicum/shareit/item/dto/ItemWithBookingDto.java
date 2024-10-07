package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
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

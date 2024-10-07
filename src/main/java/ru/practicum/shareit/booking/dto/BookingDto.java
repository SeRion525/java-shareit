package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private ItemShortDto item;
    private UserShortDto booker;
    private String status;
    private String start;
    private String end;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemShortDto {
        private Long id;
        private String name;
    }
}

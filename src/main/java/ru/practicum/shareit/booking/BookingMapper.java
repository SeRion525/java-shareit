package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "startDate", source = "start")
    @Mapping(target = "endDate", source = "end")
    Booking toBooking(BookingCreateDto bookingCreateDto);

    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "end", source = "endDate")
    BookingDto toBookingDto(Booking booking);
}

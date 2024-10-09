package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingCreateDto bookingCreateDto);

    BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBooker(Long bookerId, BookingState state);

    List<BookingDto> getBookingsByOwner(Long ownerId, BookingState state);
}

package ru.practicum.shareit.booking.handler;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CurrentBookingRequestHandler extends BookingRequestHandler {
    public CurrentBookingRequestHandler(PastBookingRequestHandler nextHandler, BookingRepository bookingRepository) {
        super(BookingState.CURRENT, nextHandler, bookingRepository);
    }

    @Override
    protected List<Booking> handle(long userId, boolean isOwner) {
        if (isOwner) {
            return bookingRepository.findCurrentByOwnerId(userId, LocalDateTime.now());
        } else {
            return bookingRepository.findCurrentByBookerId(userId, LocalDateTime.now());
        }
    }
}

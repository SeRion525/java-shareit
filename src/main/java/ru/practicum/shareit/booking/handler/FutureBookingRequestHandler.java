package ru.practicum.shareit.booking.handler;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FutureBookingRequestHandler extends BookingRequestHandler {
    public FutureBookingRequestHandler(WaitingBookingRequestHandler nextHandler, BookingRepository bookingRepository) {
        super(BookingState.FUTURE, nextHandler, bookingRepository);
    }

    @Override
    protected List<Booking> handle(long userId, boolean isOwner) {
        if (isOwner) {
            return bookingRepository.findFutureByOwnerId(userId, LocalDateTime.now());
        } else {
            return bookingRepository.findFutureByBookerId(userId, LocalDateTime.now());
        }
    }
}

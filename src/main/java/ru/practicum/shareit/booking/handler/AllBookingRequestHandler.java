package ru.practicum.shareit.booking.handler;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@Component
public class AllBookingRequestHandler extends BookingRequestHandler {
    public AllBookingRequestHandler(CurrentBookingRequestHandler nextHandler, BookingRepository bookingRepository) {
        super(BookingState.ALL, nextHandler, bookingRepository);
    }

    @Override
    protected List<Booking> handle(long userId, boolean isOwner) {
        if (isOwner) {
            return bookingRepository.findAllByOwnerId(userId);
        } else {
            return bookingRepository.findAllByBookerIdOrderByStartDateDesc(userId);
        }
    }
}

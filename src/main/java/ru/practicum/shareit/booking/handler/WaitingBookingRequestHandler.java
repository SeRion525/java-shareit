package ru.practicum.shareit.booking.handler;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

@Component
public class WaitingBookingRequestHandler extends BookingRequestHandler {
    public WaitingBookingRequestHandler(RejectedBookingRequestHandler nextHandler, BookingRepository bookingRepository) {
        super(BookingState.WAITING, nextHandler, bookingRepository);
    }

    @Override
    protected List<Booking> handle(long userId, boolean isOwner) {
        if (isOwner) {
            return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.WAITING);
        } else {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.WAITING);
        }
    }
}

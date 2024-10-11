package ru.practicum.shareit.booking.handler;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

@Component
public class RejectedBookingRequestHandler extends BookingRequestHandler {
    public RejectedBookingRequestHandler(BookingRepository bookingRepository) {
        super(BookingState.REJECTED, null, bookingRepository);
    }

    @Override
    protected List<Booking> handle(long userId, boolean isOwner) {
        if (isOwner) {
            return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.REJECTED);
        } else {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.REJECTED);
        }
    }
}

package ru.practicum.shareit.booking.handler;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@Setter
public abstract class BookingRequestHandler {
    private BookingState bookingState;
    private BookingRequestHandler nextHandler;
    protected BookingRepository bookingRepository;

    public BookingRequestHandler(BookingState bookingState, BookingRequestHandler nextHandler,
                                 @Autowired BookingRepository bookingRepository) {
        this.bookingState = bookingState;
        this.nextHandler = nextHandler;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> manageRequest(long userId, BookingState state, boolean isOwner) {
        if (state.ordinal() == bookingState.ordinal()) {
            return handle(userId, isOwner);
        }

        if (nextHandler != null) {
            return nextHandler.manageRequest(userId, state, isOwner);
        }

        throw new RuntimeException(String.format("Не удалось обработать запрос на бронирование: userId=%d, state=%s",
                userId, state));
    }

    protected abstract List<Booking> handle(long userId, boolean isOwner);
}

package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.ValidationException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState of(String state) {
        state = state.toUpperCase();

        return switch (state) {
            case "ALL" -> ALL;
            case "CURRENT" -> CURRENT;
            case "PAST" -> PAST;
            case "FUTURE" -> FUTURE;
            case "WAITING" -> WAITING;
            case "REJECTED" -> REJECTED;
            default -> throw new ValidationException("Неккоректное состояние бронирования: " + state);
        };
    }
}

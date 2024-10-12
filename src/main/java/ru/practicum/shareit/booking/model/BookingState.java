package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState from(String state) {
        state = state.toUpperCase();

        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}

package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

import static ru.practicum.shareit.util.Headers.X_SHARER_USER_ID;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                    @Valid @RequestBody BookingCreateDto bookingCreateDto) {

        if (bookingCreateDto.getStart().equals(bookingCreateDto.getEnd()) ||
                bookingCreateDto.getStart().isAfter(bookingCreateDto.getEnd())) {
            throw new ValidationException("Ошибка валидации времени бронирования");
        }

        bookingCreateDto.setBookerId(bookerId);
        return bookingService.createBooking(bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                     @PathVariable Long bookingId,
                                     @RequestParam(name = "approved") Boolean approved) {

        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {

        return bookingService.getBookingsByBooker(bookerId, BookingState.of(state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String state) {

        return bookingService.getBookingsByOwner(ownerId, BookingState.of(state));
    }
}

package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

import static ru.practicum.shareit.util.Headers.X_SHARER_USER_ID;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
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
        log.info("Create booking {}", bookingCreateDto);
        BookingDto booking = bookingService.createBooking(bookingCreateDto);
        log.info("Created booking {}", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                     @PathVariable Long bookingId,
                                     @RequestParam(name = "approved") Boolean approved) {

        log.info("Approve booking {}, state {}", bookingId, approved);
        BookingDto booking = bookingService.approveBooking(ownerId, bookingId, approved);
        log.info("Approved booking {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Get booking {}", bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                @RequestParam(name = "state", defaultValue = "ALL") @NotBlank String state) {
        log.info("Get bookings by booker {}, state {}", bookerId, state);

        BookingState bookingState = BookingState.from(state);
        if (bookingState == null) {
            throw new IllegalArgumentException("Неизвестное состояние бронирования: " + state);
        }

        return bookingService.getBookingsByBooker(bookerId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Get bookings by owner {}, state {}", ownerId, state);
        return bookingService.getBookingsByOwner(ownerId, BookingState.from(state));
    }
}

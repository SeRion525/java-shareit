package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static ru.practicum.shareit.item.ItemServiceImpl.NOT_FOUND_ITEM;
import static ru.practicum.shareit.user.UserServiceImpl.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    public static final String NOT_FOUND_BOOKING = "Не найдено бронирование с ID = ";
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(BookingCreateDto bookingCreateDto) {
        User booker = userRepository.findById(bookingCreateDto.getBookerId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + bookingCreateDto.getBookerId()));
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ITEM + bookingCreateDto.getItemId()));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с ID = " + bookingCreateDto.getItemId() + " не доступна к бронированию");
        }

        Booking booking = bookingMapper.toBooking(bookingCreateDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOKING + bookingId));

        if (!userRepository.existsById(ownerId) &&
                !booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException(String.format("Вещь с ID = %d не принадлежит пользователю с ID = %d",
                    booking.getItem().getId(), ownerId));
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBooking(Long userId, Long bookingId) {
        checkUserInRepository(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BOOKING + bookingId));

        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new AccessDeniedException("Пользователь с ID = " + userId +
                    " не является ни автором бронирования, ни владельцем вещи");
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long bookerId, BookingState state) {
        checkUserInRepository(bookerId);

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDateDesc(bookerId);
            case CURRENT -> bookingRepository.findCurrentByBookerId(bookerId);
            case PAST -> bookingRepository.findPastByBookerId(bookerId);
            case FUTURE -> bookingRepository.findFutureByBookerId(bookerId);
            case WAITING ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.WAITING);
            case REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.REJECTED);
        };

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long ownerId, BookingState state) {
        checkUserInRepository(ownerId);

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByOwnerId(ownerId);
            case CURRENT -> bookingRepository.findCurrentByOwnerId(ownerId);
            case PAST -> bookingRepository.findPastByOwnerId(ownerId);
            case FUTURE -> bookingRepository.findFutureByOwnerId(ownerId);
            case WAITING -> bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
        };

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    private void checkUserInRepository(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(NOT_FOUND_USER + userId);
        }
    }
}

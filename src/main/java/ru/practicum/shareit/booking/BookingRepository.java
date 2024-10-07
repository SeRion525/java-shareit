package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDateDesc(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and current_timestamp() between b.startDate and b.endDate " +
            "order by b.startDate desc")
    List<Booking> findCurrentByBookerId(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and current_timestamp() >= b.endDate " +
            "order by b.startDate desc")
    List<Booking> findPastByBookerId(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and current_timestamp() < b.startDate " +
            "order by b.startDate desc")
    List<Booking> findFutureByBookerId(long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(long bookerId, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.startDate desc ")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and current_timestamp() < b.startDate " +
            "order by b.startDate desc ")
    List<Booking> findCurrentByOwnerId(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and current_timestamp() >= b.endDate " +
            "order by b.startDate desc")
    List<Booking> findPastByOwnerId(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and current_timestamp() < b.startDate " +
            "order by b.startDate desc")
    List<Booking> findFutureByOwnerId(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.startDate desc ")
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and current_timestamp() >= b.endDate " +
            "order by b.endDate desc " +
            "limit 1")
    Optional<Booking> findLastByItemId(long itemId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and current_timestamp() <= b.startDate " +
            "order by b.startDate asc " +
            "limit 1")
    Optional<Booking> findNextByItemId(long itemId);
}

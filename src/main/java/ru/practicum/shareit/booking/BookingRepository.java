package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDateDesc(long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and ?2 between b.startDate and b.endDate " +
            "order by b.startDate desc")
    List<Booking> findCurrentByBookerId(long bookerId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and ?2 >= b.endDate " +
            "order by b.startDate desc")
    List<Booking> findPastByBookerId(long bookerId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and ?2 < b.startDate " +
            "order by b.startDate desc")
    List<Booking> findFutureByBookerId(long bookerId, LocalDateTime current);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(long bookerId, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.startDate desc ")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and ?2 < b.startDate " +
            "order by b.startDate desc ")
    List<Booking> findCurrentByOwnerId(long ownerId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and ?2 >= b.endDate " +
            "order by b.startDate desc")
    List<Booking> findPastByOwnerId(long ownerId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and ?2 < b.startDate " +
            "order by b.startDate desc")
    List<Booking> findFutureByOwnerId(long ownerId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and b.status = ?2 " +
            "order by b.startDate desc ")
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, BookingStatus status);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.status = 'APPROVED' and ?2 > b.endDate " +
            "order by b.endDate desc " +
            "limit 1")
    Optional<Booking> findLastByItemId(long itemId, LocalDateTime current);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and b.status = 'APPROVED' and ?2 < b.startDate " +
            "order by b.startDate asc " +
            "limit 1")
    Optional<Booking> findNextByItemId(long itemId, LocalDateTime current);

    @Query(value = """
            select BOOKINGS.* from BOOKINGS
            left join (select ITEM_ID, max(END_DATE) as max_end_date from BOOKINGS
                  where ITEM_ID in (?1) and STATUS = 'APPROVED' and ?2 >= END_DATE
                  group by ITEM_ID) as last on BOOKINGS.ITEM_ID = last.ITEM_ID
            left join (select ITEM_ID, min(START_DATE) as min_start_date from BOOKINGS
                  where ITEM_ID in (?1) and STATUS = 'APPROVED' and ?2 <= START_DATE
                  group by ITEM_ID) as next on BOOKINGS.ITEM_ID = next.ITEM_ID
            where END_DATE = last.max_end_date or START_DATE = next.min_start_date;""", nativeQuery = true)
    List<Booking> findLastAndNextByItemIds(List<Long> itemIds, LocalDateTime current);

    @Query("select case when count(*) > 0 then true else false end " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.item.id = ?2 and b.status = 'APPROVED' and b.endDate < ?3")
    boolean isMakeBooking(Long userId, Long itemId, LocalDateTime current);
}

package bookingservice.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import bookingservice.model.Booking;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    @Query("{ 'showtimeId': ?0, 'seatNumbers': { $in: ?1 }, 'status': 'BOOKED' }")
    boolean existsByShowtimeIdAndSeatNumbersIn(String showtimeId, List<String> seatNumbers);
}

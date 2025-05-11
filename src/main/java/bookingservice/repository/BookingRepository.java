package bookingservice.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import bookingservice.model.Booking;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    boolean existsByShowtimeIdAndSeatNumbersIn(String showtimeId, List<String> seatNumbers, String status);
}

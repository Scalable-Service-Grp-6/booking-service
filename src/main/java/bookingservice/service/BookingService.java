package bookingservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bookingservice.exception.BookingException;
import bookingservice.model.Booking;
import bookingservice.model.BookingRequest;
import bookingservice.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(BookingRequest request) {
    	boolean seatsAlreadyBooked = bookingRepository.existsByShowtimeIdAndSeatNumbersIn(
                request.getShowtimeId(), request.getSeatNumbers());

        if (seatsAlreadyBooked) {
            throw new BookingException("One or more selected seats are already booked.");
        }
        
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setMovieId(request.getMovieId());
        booking.setShowtimeId(request.getShowtimeId());
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setStatus("BOOKED");
        booking.setBookingTime(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public boolean cancelBooking(String id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByMovie(String movieId) {
        return bookingRepository.findAll()
                .stream()
                .filter(b -> b.getMovieId().equals(movieId))
                .toList();
    }
    public boolean updatePaymentStatus(String id, String status) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setPaymentStatus(status.toUpperCase());
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

}

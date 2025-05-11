package bookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import bookingservice.exception.BookingException;
import bookingservice.model.Booking;
import bookingservice.model.BookingRequest;
import bookingservice.model.PaymentRequest;
import bookingservice.model.PaymentResponse;
import bookingservice.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PAYMENT_SERVICE_URL = "./api/payments";

    public Booking createBooking(BookingRequest request) {
        // Step 1: Create a unique booking request ID (you can use UUID for this)
        String bookRequestId = UUID.randomUUID().toString();
        
        // Step 2: Prepare the payment request object
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookRequestId(bookRequestId);
        paymentRequest.setAmount(500); // Example amount, should be dynamic based on booking
        paymentRequest.setMethodType("Credit Card");
        paymentRequest.setBankDetails("SBI, BANGALORE");
        paymentRequest.setPaymentDetails("123123123123123213"); // Example credit card number
        paymentRequest.setTimestamp(LocalDateTime.now().toString());

        // Step 3: Send payment request to the payment service
        ResponseEntity<PaymentResponse> paymentResponse = sendPaymentRequest(paymentRequest);

        // Step 4: If payment was successful, create the booking
        if (paymentResponse != null && paymentResponse.getStatusCode() == HttpStatus.OK) {
            boolean seatsAlreadyBooked = bookingRepository.existsByShowtimeIdAndSeatNumbersIn(request.getShowtimeId(), request.getSeatNumbers(),"BOOKED");
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
        } else {
            throw new BookingException("Payment failed, cannot create booking.");
        }
    }

    // Helper method to send a payment request
    private ResponseEntity<PaymentResponse> sendPaymentRequest(PaymentRequest paymentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);
        return restTemplate.exchange(PAYMENT_SERVICE_URL, HttpMethod.POST, entity, PaymentResponse.class);
    }

    // Other methods like getBookingsByUser, cancelBooking, etc.
    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findByUserId(userId);
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

    // ... other existing methods
}

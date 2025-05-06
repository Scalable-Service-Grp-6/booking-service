package bookingservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bookingservice.model.Booking;
import bookingservice.model.BookingRequest;
import bookingservice.service.BookingService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<Booking> bookTicket(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable String id) {
        boolean result = bookingService.cancelBooking(id);
        return result ? ResponseEntity.ok("Cancelled") : ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(
            @RequestParam(required = false) String movieId
    ) {
        if (movieId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByMovie(movieId));
        } else {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
    }
    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam String status) {

        boolean updated = bookingService.updatePaymentStatus(id, status);
        return updated ? ResponseEntity.ok("Payment status updated")
                       : ResponseEntity.notFound().build();
    }
}

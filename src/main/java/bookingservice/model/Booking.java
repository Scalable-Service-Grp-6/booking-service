package bookingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String movieId;
    private String showtimeId;
    private List<String> seatNumbers;
    private String status; //States:- "BOOKED", "CANCELLED"
    private String paymentStatus = "PENDING";   //States:- "PENDING", "PAID", "FAILED"
    private LocalDateTime bookingTime;

    // Getters and setters omitted for brevity
}

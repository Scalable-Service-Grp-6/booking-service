package bookingservice.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
public class BookingRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Movie ID is required")
    private String movieId;

    @NotBlank(message = "Showtime ID is required")
    private String showtimeId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> seatNumbers;
}

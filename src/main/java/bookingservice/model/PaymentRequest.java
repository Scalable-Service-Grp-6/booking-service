package bookingservice.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private String bookRequestId;
    private int amount;
    private String methodType;
    private String bankDetails;
    private String paymentDetails;
    private String timestamp;
}

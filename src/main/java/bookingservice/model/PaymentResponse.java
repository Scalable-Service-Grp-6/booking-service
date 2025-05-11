package bookingservice.model;

import lombok.Data;

@Data
public class PaymentResponse {
    private String bookRequestId;
    private String paymentStatus;
    private String failureReason;
    private String transactionId;
}


package com.calmora.DTO.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private String orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private int appointmentId;
    private Long therapistId;
    private LocalDateTime createdAt;
}

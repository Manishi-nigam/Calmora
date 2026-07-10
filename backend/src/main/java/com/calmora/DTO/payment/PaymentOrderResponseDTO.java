package com.calmora.DTO.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderResponseDTO {
    private String orderId;
    private String razorpayOrderId;
    private BigDecimal amount;
    private String currency;
    private String status;
}

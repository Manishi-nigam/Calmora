package com.calmora.DTO.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderRequestDTO {
    private BigDecimal amount;
    private String currency;
    private int appointmentId;
    private Long therapistId;
}

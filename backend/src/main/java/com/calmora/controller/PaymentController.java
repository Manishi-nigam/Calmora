package com.calmora.controller;

import com.calmora.DTO.payment.PaymentOrderRequestDTO;
import com.calmora.DTO.payment.PaymentOrderResponseDTO;
import com.calmora.DTO.payment.PaymentResponseDTO;
import com.calmora.DTO.payment.VerifyPaymentRequestDTO;
import com.calmora.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<PaymentOrderResponseDTO> createOrder(
            @RequestBody PaymentOrderRequestDTO request,
            Principal principal) {

        PaymentOrderResponseDTO response = paymentService.createOrder(request, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(
            @RequestBody VerifyPaymentRequestDTO request) {

        PaymentResponseDTO response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @PathVariable String orderId) {

        PaymentResponseDTO response = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PaymentResponseDTO>> getUserPayments(
            Principal principal) {

        List<PaymentResponseDTO> payments = paymentService.getPaymentsByUser(principal.getName());
        return ResponseEntity.ok(payments);
    }
}

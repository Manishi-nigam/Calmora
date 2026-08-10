package com.calmora.service;

import com.calmora.DTO.payment.PaymentOrderRequestDTO;
import com.calmora.DTO.payment.PaymentOrderResponseDTO;
import com.calmora.DTO.payment.PaymentResponseDTO;
import com.calmora.DTO.payment.VerifyPaymentRequestDTO;
import com.calmora.model.Enum.status;
import com.calmora.model.Payment;
import com.calmora.model.User;
import com.calmora.repository.PaymentRepository;
import com.calmora.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RazorpayClient razorpayClient;
    private final String razorpayKeySecret;

    public PaymentService(
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            @Value("${razorpay.key.id}") String razorpayKeyId,
            @Value("${razorpay.key.secret}") String razorpayKeySecret
    ) throws RazorpayException {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        this.razorpayKeySecret = razorpayKeySecret;
    }

    public PaymentOrderResponseDTO createOrder(PaymentOrderRequestDTO request, String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Razorpay expects amount in paise (smallest currency unit)
            int amountInPaise = request.getAmount().multiply(BigDecimal.valueOf(100)).intValue();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
            orderRequest.put("receipt", "rcpt_" + UUID.randomUUID().toString().substring(0, 8));

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            Payment payment = new Payment();
            payment.setOrderId(UUID.randomUUID().toString());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
            payment.setStatus(status.PENDING);
            payment.setAppointmentId((long) request.getAppointmentId());
            payment.setTherapistId(request.getTherapistId());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUser(user);

            paymentRepository.save(payment);

            return new PaymentOrderResponseDTO(
                    payment.getOrderId(),
                    razorpayOrder.get("id"),
                    request.getAmount(),
                    payment.getCurrency(),
                    status.PENDING.name()
            );
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    public PaymentResponseDTO verifyPayment(VerifyPaymentRequestDTO request) {
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found for Razorpay order: " + request.getRazorpayOrderId()));

        try {
            // Verify the payment signature using Razorpay utility
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

            if (isValid) {
                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
                payment.setRazorpaySignature(request.getRazorpaySignature());
                payment.setStatus(status.SUCCESS);
            } else {
                payment.setStatus(status.FAILED);
            }
        } catch (RazorpayException e) {
            payment.setStatus(status.FAILED);
            throw new RuntimeException("Payment verification failed: " + e.getMessage(), e);
        }

        paymentRepository.save(payment);
        return toResponseDTO(payment);
    }

    public PaymentResponseDTO getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toResponseDTO(payment);
    }

    public List<PaymentResponseDTO> getPaymentsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private PaymentResponseDTO toResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrderId(),
                payment.getRazorpayOrderId(),
                payment.getRazorpayPaymentId(),
                payment.getStatus() != null ? payment.getStatus().name() : null,
                payment.getAmount(),
                payment.getCurrency(),
                payment.getAppointmentId() != null ? payment.getAppointmentId().intValue() : 0,
                payment.getTherapistId(),
                payment.getCreatedAt()
        );
    }
}

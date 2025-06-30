package com.data.controller;

import com.data.dto.PaymentResponseDto;
import com.data.entity.PaymentOrder; // ✅ Use your own entity
import com.data.repository.PaymentOrderRepository;
import com.data.service.PaymentService;
import com.data.util.PaymentUtil;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentOrderRepository orderRepository;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam int amount) {
        try {
            String order = paymentService.createOrder(amount);
            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
        	e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentResponseDto dto) {
        boolean isValid = PaymentUtil.verifySignature(
                dto.getOrderId(),
                dto.getPaymentId(),
                dto.getSignature(),
                "nAvMifuA3nxGqusm9x20qNXR"
        );

        if (isValid) {
            PaymentOrder order = new PaymentOrder(); // ✅ Use correct class
            order.setOrderId(dto.getOrderId());
            order.setPaymentId(dto.getPaymentId());
            order.setStatus("SUCCESS");
            order.setAmount(dto.getAmount()/100);
            order.setDateTime(LocalDateTime.now());
            orderRepository.save(order);

            return ResponseEntity.ok("Payment Verified and Saved");
        } else {
            return ResponseEntity.status(400).body("Invalid Payment Signature");
        }
    }
}

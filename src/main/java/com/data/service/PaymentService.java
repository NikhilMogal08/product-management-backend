package com.data.service;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {
    private RazorpayClient razorpay;

    @PostConstruct
    public void init() throws RazorpayException {
        razorpay = new RazorpayClient("rzp_test_uFQQELlFCtu95s", "nAvMifuA3nxGqusm9x20qNXR");
    }

    public String createOrder(int amount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount ); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", UUID.randomUUID().toString());

        Order order = razorpay.orders.create(orderRequest);
        return order.toString(); // return order details
    }
}


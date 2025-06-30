package com.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.data.entity.Order;
import com.data.entity.OrderItem;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(String toEmail, Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmation");

            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(order.getUser().getName()).append(",\n\n");
            body.append("Thank you for your order!\n");
            body.append("Order ID: ").append(order.getId()).append("\n\n");

            for (OrderItem item : order.getItems()) {
                body.append("Product Name: ").append(item.getProduct().getName()).append("\n");
                body.append("Description: ").append(item.getProduct().getDescription()).append("\n");
                body.append("Quantity: ").append(item.getQuantity()).append("\n");
                body.append("Price: ₹").append(item.getPrice()).append("\n\n");
            }
            body.append("Payment Method: ").append(order.getPaymentMethod()).append("\n")
            .append("Payment Status: ").append(order.getPaymentStatus()).append("\n\n");
            body.append("Total Price: ₹").append(order.getTotalPrice()).append("\n\n");
            body.append("We'll process your order shortly.\n\nRegards,\nYour Team");

            message.setText(body.toString());

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("OTP Verification - Product Management System");
        message.setText("Your OTP for email verification is: " + otp);
        mailSender.send(message);
    }
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

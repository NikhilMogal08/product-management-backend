package com.data.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.OrderRequest;
import com.data.entity.Order;
import com.data.entity.OrderItem;
import com.data.entity.Product;
import com.data.entity.User;
import com.data.repository.OrderRepository;
import com.data.repository.ProductRepository;
import com.data.repository.UserRepository;
import com.data.service.*;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SmsService smsService;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        // Fetch the user
        User user = userRepository.findById(orderRequest.getUserId())
                                  .orElseThrow(() -> new RuntimeException("User not found"));

        // Create Order object
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        // ✅ Sort cart items by product ID to avoid circular deadlocks
        orderRequest.getCartItems().sort(Comparator.comparing(OrderRequest.CartItem::getProductId));

        // Step 1: Validate stock and create OrderItems (with locking)
        for (OrderRequest.CartItem cartItem : orderRequest.getCartItems()) {
            Product product = productRepository.findByIdForUpdate(cartItem.getProductId())
                                               .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Product '" + product.getName() + "' is out of stock or insufficient quantity.");
            }

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice() * cartItem.getQuantity());
            item.setOrder(order);

            orderItems.add(item);
            total += item.getPrice();
            
         // ✅ Set payment method and status
            if ("Online".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
                order.setPaymentMethod("Online");
                order.setPaymentStatus("Paid");
                order.setRazorpayPaymentId(orderRequest.getRazorpayPaymentId());
            } else {
                order.setPaymentMethod("COD");
                order.setPaymentStatus("Pending");
            }

        }

        // Step 2: Save order
        order.setItems(orderItems);
        order.setTotalPrice(total);
        orderRepository.save(order);

        // ✅ Step 3: Deduct stock after saving order to reduce lock time
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Step 4: Send confirmation email
        StringBuilder body = new StringBuilder();
        body.append("Hi ").append(user.getName()).append(",\n\n");
        body.append("Thank you for your order! Here are the details:\n\n");

        for (OrderItem item : orderItems) {
            body.append("Product: ").append(item.getProduct().getName()).append("\n")
                .append("Quantity: ").append(item.getQuantity()).append("\n")
                .append("Price: ₹").append(item.getPrice()).append("\n\n");
        }

        body.append("Total Price: ₹").append(order.getTotalPrice()).append("\n\n")
            .append("Order Date: ").append(order.getOrderDate()).append("\n")
            .append("Payment Method: ").append(order.getPaymentMethod()).append("\n")
            .append("Payment Status: ").append(order.getPaymentStatus()).append("\n\n")
            .append("We will notify you once your order is shipped.\n\n")
            .append("Best regards,\nProduct Management Team");

        emailService.sendOrderConfirmationEmail(user.getEmail(), order);
        
     // Step 5: Send SMS confirmation
//        String smsMessage = "Hi " + user.getName() +
//                            ", your order has been placed! Total: ₹" + order.getTotalPrice() +
//                            ". We’ll notify you when it ships. - Product Team";

        // Ensure user's phone number is valid, e.g., +91XXXXXXXXXX
     // Format mobile number to E.164 format (e.g., +91XXXXXXXXXX)
//        String mobile = user.getMobileNumber();
//        if (!mobile.startsWith("+")) {
//            mobile = "+91" + mobile; // Assuming Indian numbers; change based on your region
//        }
//
//        smsService.sendSms(mobile, smsMessage);

        
//        smsService.sendWhatsApp(user.getMobileNumber(), smsMessage);



        return "Order placed successfully! Confirmation sent to " + user.getEmail();
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public boolean updatePaymentStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setPaymentStatus(status);
            orderRepository.save(order);

            // ✅ Send email when status is marked as "Success"
            if ("Success".equalsIgnoreCase(status)) {
                sendOrderDeliveredEmail(order);
            }

            return true;
        }

        return false;
    }

    private void sendOrderDeliveredEmail(Order order) {
        User user = order.getUser();

        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Hi ").append(user.getName()).append(",\n\n");
        emailBody.append("Your order (ID: ").append(order.getId()).append(") has been delivered successfully.\n");
        emailBody.append("We have received your payment. Thank you for shopping with us!\n\n");

        emailBody.append("Order Details:\n");
        for (OrderItem item : order.getItems()) {
            emailBody.append("- ")
                     .append(item.getProduct().getName())
                     .append(" (Qty: ").append(item.getQuantity()).append(", Price: ₹")
                     .append(item.getPrice()).append(")\n");
        }

        emailBody.append("\nTotal Paid: ₹").append(order.getTotalPrice()).append("\n");
        emailBody.append("Payment Method: ").append(order.getPaymentMethod()).append("\n");
        emailBody.append("Payment Status: ").append(order.getPaymentStatus()).append("\n\n");

        emailBody.append("We hope to serve you again!\n");
        emailBody.append("Best regards,\nProduct Management Team");

        emailService.sendEmail(user.getEmail(), "Order Delivered Successfully", emailBody.toString());
    }



}

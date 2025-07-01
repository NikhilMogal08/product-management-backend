package com.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.data.dto.OrderRequest;
import com.data.entity.Order;
import com.data.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Adjust the origin as needed
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint to place an order
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Call the service to place the order
        String response = orderService.placeOrder(orderRequest);
        
        // Return the response (success or failure message)
        return ResponseEntity.ok(response);
    }

    // Endpoint to get all orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        // Fetch the orders by user from the service
        List<Order> orders = orderService.getOrdersByUser(userId);
        
        // Return the list of orders
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{orderId}/payment-status")
    public ResponseEntity<String> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam("status") String status) {
        
        boolean updated = orderService.updatePaymentStatus(orderId, status);
        
        if (updated) {
            return ResponseEntity.ok("Payment status updated to: " + status);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }


}

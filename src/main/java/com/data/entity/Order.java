package com.data.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;

@Entity
@Table(name = "orders") // 'order' is reserved in SQL
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String productDescription;
    private LocalDateTime orderDate;
    private double totalPrice;
    
    @Column(name = "payment_method")
    private String paymentMethod; // e.g., "Online" or "Cash on Delivery"

    @Column(name = "payment_status")
    private String paymentStatus; // e.g., "Paid" or "Pending"

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId; // Razorpay payment ID if applicable


    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("order")
    private List<OrderItem> items;

	public Order() {
		super();
	}
	

	public Order(Long id, String productName, String productDescription, LocalDateTime orderDate, double totalPrice,
			String paymentMethod, String paymentStatus, String razorpayPaymentId, User user, List<OrderItem> items) {
		super();
		this.id = id;
		this.productName = productName;
		this.productDescription = productDescription;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.razorpayPaymentId = razorpayPaymentId;
		this.user = user;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}


	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
    
    
}


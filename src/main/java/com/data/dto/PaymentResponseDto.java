package com.data.dto;

public class PaymentResponseDto {
    private String orderId;
    private String paymentId;
    private String signature;
    private int amount;
	public PaymentResponseDto() {
		super();
	}
	public PaymentResponseDto(String orderId, String paymentId, String signature, int amount) {
		super();
		this.orderId = orderId;
		this.paymentId = paymentId;
		this.signature = signature;
		this.amount = amount;
	}
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
    
    
}

package com.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.entity.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}


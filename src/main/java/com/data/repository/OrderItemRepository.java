package com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{

	List<OrderItem> findByProductId(Long id);
	
}

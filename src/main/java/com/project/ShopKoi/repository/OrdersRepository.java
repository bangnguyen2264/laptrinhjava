package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderNumber(UUID orderNumber); 
    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = ?1")
    long countByStatus(OrderStatus status);
}

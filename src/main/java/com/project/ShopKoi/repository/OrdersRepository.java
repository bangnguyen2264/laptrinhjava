package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderNumber(UUID orderNumber); // Thay đổi String thành UUID
}

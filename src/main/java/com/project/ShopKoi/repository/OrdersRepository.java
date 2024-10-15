package com.project.ShopKoi.repository;

import com.project.ShopKoi.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}

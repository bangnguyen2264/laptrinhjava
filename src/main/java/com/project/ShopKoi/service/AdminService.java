package com.project.ShopKoi.service;

import com.project.ShopKoi.model.enums.OrderStatus;
import java.util.Map;

public interface AdminService {

    String changeUserRole(Long id,String roleName);
    Map<String, Long> getUserStatistics();
    Map<OrderStatus, Long> getOrderStatistics();
}

package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.enums.OrderStatus;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.List;
import java.util.Map;

public interface AdminService {
    String changeUserRole(Long id,String roleName);
    Map<String, Long> getUserStatistics();
    Map<OrderStatus, Long> getOrderStatistics();
}

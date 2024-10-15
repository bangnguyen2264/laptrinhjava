package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.form.OrdersForm;

import java.util.List;

public interface OrderService {
    OrdersDto createOrder(OrdersForm orderForm);
    OrdersDto getOrderById(Long id);
    List<OrdersDto> getAllOrders();
    List<OrdersDto> getMyOrder();
    OrdersDto deleteOrder(Long id);
    OrdersDto changeStatusOrder(Long id, String status);

}

package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.dto.PriceTableDto;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.FeedbackForm;
import com.project.ShopKoi.model.form.OrdersForm;

import java.util.List;

public interface OrderService {
    OrdersDto createOrder(OrdersForm orderForm);
    OrdersDto getOrderById(Long id);
    List<OrdersDto> getAllOrders();
    List<OrdersDto> getMyOrder();
    String deleteOrder(Long id);
    OrdersDto changeStatusOrder(Long id, OrderStatus status, FeedbackForm feedbackForm);
    List<PriceTableDto> showPriceTable(OrdersForm ordersForm);

}

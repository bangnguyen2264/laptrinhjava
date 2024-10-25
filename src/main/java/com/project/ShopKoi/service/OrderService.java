package com.project.ShopKoi.service;

import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.dto.PriceTableDto;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.FeedbackForm;
import com.project.ShopKoi.model.form.OrdersForm;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrdersDto createOrder(OrdersForm orderForm);
    OrdersDto getOrderById(Long id);
    OrdersDto getOrderByOrderNumber(UUID orderNumber);
    List<OrdersDto> getAllOrders(int page, int size);
    OrdersDto changeStatusOrder(Long id, OrderStatus status);
    List<OrdersDto> getMyOrder();
    OrdersDto getMyOrderById(Long id);
    String deleteOrder(Long id);
    List<PriceTableDto> showPriceTable(OrdersForm ordersForm);
    void sendFeedback(Long id, FeedbackForm feedbackForm);
    List<OrdersDto> getMyDeliverOrder();
    void removeOrderFromDelivery(Long id);
    void assignDelivery(Long orderId, Long deliveryId);
    void updateDelivery(Long orderId, Long deliveryId);



}

package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.Orders;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class OrdersDto implements Serializable {
    private Long id;
    private UUID orderNumber;
    private String title;
    private int quantity;
    private double weight;
    private String status;
    private String method;
    private String origin;
    private String destination;
    private double price;
    private String note;

    public static OrdersDto toDto(Orders orders) {
        return OrdersDto.builder()
                .id(orders.getId())
                .orderNumber(orders.getOrderNumber())
                .title(orders.getTitle())
                .quantity(orders.getQuantity())
                .weight(orders.getWeight())
                .status(orders.getStatus().toString())
                .method(orders.getMethod().toString())
                .origin(orders.getOrigin().toString())
                .destination(orders.getDestination().toString())
                .price(orders.getPrice())
                .note(orders.getNote())
                .build();
    }
}

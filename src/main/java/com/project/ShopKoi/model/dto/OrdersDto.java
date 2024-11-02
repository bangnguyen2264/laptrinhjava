package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private BigDecimal price;
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
                .price(BigDecimal.valueOf(orders.getPrice()))
                .note(orders.getNote())
                .build();
    }
}

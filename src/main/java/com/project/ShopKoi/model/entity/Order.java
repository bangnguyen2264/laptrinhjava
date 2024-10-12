package com.project.ShopKoi.model.entity;

import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.enums.TransportMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private UUID orderNumber;
    private String title;
    private int quantity;
    private double weight;
    @ManyToOne
    private Address origin;
    @ManyToOne
    private Address destination;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    private TransportMethod method;
    private String note;
}

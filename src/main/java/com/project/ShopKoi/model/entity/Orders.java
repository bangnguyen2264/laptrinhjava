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
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private UUID orderNumber;
    private String title;
    private int quantity;
    private double weight;
    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "origin_address_id")
    private Address origin;

    @ManyToOne
    @JoinColumn(name = "destination_address_id")
    private Address destination;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private TransportMethod method;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "deliver_id")
    private User deliver;
    private String feedbackMessage;
    private double rating;
}


package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.OrdersForm;
import com.project.ShopKoi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody @Valid OrdersForm order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/me")
    public ResponseEntity getMyOrder() {
        return ResponseEntity.ok(orderService.getMyOrder());
    }

    @GetMapping
    public ResponseEntity getAllOrder(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("{orderId}")
    public ResponseEntity getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PatchMapping("/change-status/{orderId}")
    public ResponseEntity changeStatusOrder(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.changeStatusOrder(orderId, status));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }

    @PostMapping("/price-table")
    public ResponseEntity getTablePrice(@RequestBody OrdersForm ordersForm) {
        return ResponseEntity.ok(orderService.showPriceTable(ordersForm));
    }
}

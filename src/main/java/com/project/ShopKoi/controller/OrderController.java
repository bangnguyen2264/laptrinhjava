package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.FeedbackForm;
import com.project.ShopKoi.model.form.OrdersForm;
import com.project.ShopKoi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Tạo mới đơn hàng
    @PostMapping
    public ResponseEntity createOrder(@RequestBody @Valid OrdersForm order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    // Lấy tất cả đơn hàng của người dùng hiện tại
    @GetMapping("/me")
    public ResponseEntity getMyOrder() {
        return ResponseEntity.ok(orderService.getMyOrder());
    }

    // Lấy tất cả đơn hàng
    @GetMapping
    public ResponseEntity getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    // Lấy đơn hàng theo ID
    @GetMapping("{orderId}")
    public ResponseEntity getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // Thay đổi trạng thái đơn hàng
    @PatchMapping("/change-status/{orderId}")
    public ResponseEntity changeStatusOrder(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.changeStatusOrder(orderId, status));
    }

    // Xóa đơn hàng
    @DeleteMapping("/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }

    // Xem bảng giá
    @PostMapping("/price-table")
    public ResponseEntity getTablePrice(@RequestBody OrdersForm ordersForm) {
        return ResponseEntity.ok(orderService.showPriceTable(ordersForm));
    }

    // Tìm đơn hàng theo mã số
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity findOrderByOrderNumber(@PathVariable UUID orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    // Gửi phản hồi cho đơn hàng
    @PostMapping("/{orderId}/feedback")
    public ResponseEntity sendFeedback(@PathVariable Long orderId, @RequestBody @Valid FeedbackForm feedbackForm) {
        orderService.sendFeedback(orderId, feedbackForm);
        return ResponseEntity.ok("Feedback sent successfully");
    }

    // Gán đơn hàng cho nhân viên giao hàng
    @PostMapping("/deliver/{orderId}")
    public ResponseEntity assignDelivery(@PathVariable Long orderId, @RequestParam Long deliveryId) {
        orderService.assignDelivery(orderId, deliveryId);
        return ResponseEntity.ok("Delivery assigned successfully");
    }

    // Cập nhật nhân viên giao hàng cho đơn hàng
    @PutMapping("/deliver/update/{orderId}")
    public ResponseEntity updateDelivery(@PathVariable Long orderId, @RequestParam Long deliveryId) {
        orderService.updateDelivery(orderId, deliveryId);
        return ResponseEntity.ok("Delivery updated successfully");
    }

    // Lấy tất cả đơn hàng của người giao hàng
    @GetMapping("/deliver/me")
    public ResponseEntity getMyDeliverOrder() {
        return ResponseEntity.ok(orderService.getMyDeliverOrder());
    }
    @PostMapping("/deliver/me/remove/{orderId}")
    public ResponseEntity removeOrderFromDelivery(@PathVariable Long orderId) {
        orderService.removeOrderFromDelivery(orderId);
        return ResponseEntity.ok("Delivery removed successfully");
    }

    @GetMapping("/me/{orderId}")
    public ResponseEntity getMyOrderById(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getMyOrderById(orderId));
    }
}

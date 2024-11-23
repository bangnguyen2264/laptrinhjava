package com.project.ShopKoi.controller;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.service.impl.AdminServiceImpl;
import com.project.ShopKoi.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/user-id/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size    ) {
        return ResponseEntity.ok(userServiceImpl.getAllUsers(page, size));
    }

    @GetMapping("/users-role/{roleName}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String roleName) {
        List<UserDto> users = userServiceImpl.getAllUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }
    @PutMapping("/change-role/{userId}")
    public ResponseEntity<String> updateUserRole(@PathVariable Long userId,@RequestParam String roleName) {
        String response = adminServiceImpl.changeUserRole(userId, roleName );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-statistics")
    public ResponseEntity<Map<String, Long>> getUserStatistics() {
        Map<String, Long> statistics = adminServiceImpl.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/order-statistics")
    public ResponseEntity<Map<OrderStatus, Long>> getOrderStatistics() {
        Map<OrderStatus, Long> statistics = adminServiceImpl.getOrderStatistics();
        return ResponseEntity.ok(statistics);
    }

}

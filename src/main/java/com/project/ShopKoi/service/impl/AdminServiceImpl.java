package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.model.dto.UserDto;
import com.project.ShopKoi.model.entity.Role;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.repository.OrdersRepository;
import com.project.ShopKoi.repository.RoleRepository;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrdersRepository ordersRepository;


    @Override
    public String changeUserRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getRole().getName().equals("ROLE_USER")) {
            return "User does not have ROLE_USER"; // Trả về thông báo nếu không phải ROLE_USER
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Admin role not found"));

        user.setRole(adminRole);
        userRepository.save(user);
        return "User role updated to ROLE_ADMIN successfully";
    }

    @Override
    public Map<String, Long> getUserStatistics() {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role USER not found")); // Kiểm tra Role_USER
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Role ADMIN not found")); // Kiểm tra Role_ADMIN

        long userCount = userRepository.countByRole(userRole); // Đếm số người dùng
        long adminCount = userRepository.countByRole(adminRole); // Đếm số admin

        Map<String, Long> statistics = new HashMap<>();
        statistics.put("userCount", userCount);
        statistics.put("adminCount", adminCount);

        return statistics;
    }

    @Override
    public Map<OrderStatus, Long> getOrderStatistics() {
        Map<OrderStatus, Long> statistics = new HashMap<>();

        for (OrderStatus status : OrderStatus.values()) {
            long count = ordersRepository.countByStatus(status); // Đếm số lượng đơn hàng theo từng trạng thái
            statistics.put(status, count);
        }

        return statistics;
    }


}

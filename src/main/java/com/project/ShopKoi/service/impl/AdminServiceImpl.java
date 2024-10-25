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
    public String changeUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getRole().getName().equals("ROLE_USER")) {
            return "User does not have ROLE_USER";
        }
        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.setRole(newRole);
        userRepository.save(user);
        return "User role updated to " + roleName + "successfully";
    }

    @Override
    public Map<String, Long> getUserStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            long count = userRepository.countByRole(role);
            statistics.put(role.getName(), count);
        }

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

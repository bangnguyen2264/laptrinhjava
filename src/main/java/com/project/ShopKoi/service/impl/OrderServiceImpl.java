package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.AddressForm;
import com.project.ShopKoi.model.form.AddressItemForm;
import com.project.ShopKoi.model.form.OrdersForm;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.repository.AddressRepository;
import com.project.ShopKoi.repository.OrdersRepository;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.OrderService;
import com.project.ShopKoi.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;
    private final UserRepository userRepository;

    @Override
    public OrdersDto createOrder(OrdersForm orderForm) {
        User user = userRepository.findByEmail(UserUtils.getMe()).orElseThrow();
        Address origin = Address.builder()
                .name(orderForm.getOrigin().getName())
                .addressItems(convertAddressItems(orderForm.getOrigin()))
                .build();
        Address destination = Address.builder()
                .name(orderForm.getDestination().getName())
                .addressItems(convertAddressItems(orderForm.getDestination()))
                .build();
        addressRepository.saveAll(List.of(origin, destination));
        Orders orders = Orders.builder()
                .orderNumber(UUID.randomUUID())
                .title("Đơn hàng của "+ UserUtils.getMe())
                .origin(origin)
                .destination(destination)
                .quantity(orderForm.getQuantity())
                .weight(orderForm.getWeight())
                .status(OrderStatus.PENDING)
                .method(orderForm.getMethod())
                .user(user)
                .note(orderForm.getNote())
                .build();
        user.getOrders().add(orders);
        userRepository.save(user);
        orderRepository.save(orders);
        return OrdersDto.toDto(orders);
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        return null;
    }

    @Override
    public List<OrdersDto> getAllOrders() {
        return List.of();
    }

    @Override
    public List<OrdersDto> getMyOrder() {
        return List.of();
    }

    @Override
    public OrdersDto deleteOrder(Long id) {
        return null;
    }

    @Override
    public OrdersDto changeStatusOrder(Long id, String status) {
        return null;
    }

    private List<AddressItem> convertAddressItems(AddressForm addressForm) {
        AddressItem country = addressItemRepository
                .findByName(addressForm.getCountry())
                .orElseThrow(() -> new NotFoundException("Country " + addressForm.getCountry() + " not found"));
        AddressItem city = addressItemRepository
                .findByName(addressForm.getCity())
                .orElseThrow(() -> new NotFoundException("City " + addressForm.getCity() + " not found"));
        AddressItem district = addressItemRepository
                .findByName(addressForm.getDistrict())
                .orElseThrow(() -> new NotFoundException("District " + addressForm.getDistrict() + " not found"));
        return List.of(district, city, country);
    }
}

package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.form.AddressForm;
import com.project.ShopKoi.model.form.OrdersForm;
import com.project.ShopKoi.repository.AddressItemRepository;
import com.project.ShopKoi.repository.AddressRepository;
import com.project.ShopKoi.repository.OrdersRepository;
import com.project.ShopKoi.repository.UserRepository;
import com.project.ShopKoi.service.OrderService;
import com.project.ShopKoi.utils.ShipFee;
import com.project.ShopKoi.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;
    private final UserRepository userRepository;

    @Override
    @CachePut(value = "orders", key = "#result.orderNumber") // Cache the created order
    public OrdersDto createOrder(OrdersForm orderForm) {
        User user = userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check and get or create addresses
        Address origin = createOrGetAddress(orderForm.getOrigin());
        Address destination = createOrGetAddress(orderForm.getDestination());

        // Build the order
        Orders orders = buildOrder(orderForm, user, origin, destination);
        orderRepository.save(orders);
        return OrdersDto.toDto(orders);
    }

    @Override
    @Cacheable(value = "orders", key = "#id") // Cache the order by ID
    public OrdersDto getOrderById(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrdersDto.toDto(order);
    }

    @Override
    @Cacheable(value = "orders") // Cache all orders
    public List<OrdersDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrdersDto::toDto)
                .toList();
    }

    @Override
    @Cacheable(value = "orders", key = "'myOrders_' + T(com.project.ShopKoi.utils.UserUtils).getMe()") // Cache user's orders
    public List<OrdersDto> getMyOrder() {
        User user = userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return user.getOrders().stream()
                .map(OrdersDto::toDto)
                .toList();
    }

    @Override
    @CacheEvict(value = "orders", key = "#id") // Evict the order from cache on delete
    public OrdersDto deleteOrder(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.delete(order);
        return OrdersDto.toDto(order);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id") // Evict the order from cache on status change
    public OrdersDto changeStatusOrder(Long id, String status) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return OrdersDto.toDto(order);
    }

    private Address createOrGetAddress(AddressForm addressForm) {
        // Trim the address name
        String addressName = addressForm.getName().trim();

        // Get address items
        List<AddressItem> addressItems = convertAddressItems(addressForm);

        // Check if the address already exists
        Optional<Address> existingAddress = addressRepository.findByNameAndAddressItems(addressName, addressItems);

        // If the address exists, return it; otherwise, create a new one
        return existingAddress.orElseGet(() -> {
            Address newAddress = Address.builder()
                    .name(addressName)
                    .addressItems(addressItems)
                    .build();
            return addressRepository.save(newAddress); // Save the new address
        });
    }

    private Orders buildOrder(OrdersForm orderForm, User user, Address origin, Address destination) {
        Orders orders = Orders.builder()
                .orderNumber(UUID.randomUUID())
                .title("Đơn hàng của " + UserUtils.getMe())
                .origin(origin)
                .destination(destination)
                .quantity(orderForm.getQuantity())
                .weight(orderForm.getWeight())
                .status(OrderStatus.PENDING)
                .method(orderForm.getMethod())
                .user(user)
                .note(orderForm.getNote())
                .build();
        orders.setPrice(ShipFee.calculate(orders));
        user.getOrders().add(orders);
        userRepository.save(user); // Update user's orders
        return orders;
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

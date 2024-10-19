package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.BadRequestException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final AddressRepository addressRepository;
    private final AddressItemRepository addressItemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrdersDto createOrder(OrdersForm orderForm) {
        User user = userRepository.findByEmail(UserUtils.getMe())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check and get or create addresses
        Address origin = createOrGetAddress(orderForm.getOrigin());
        log.info("Origin address: {}", origin);
        Address destination = createOrGetAddress(orderForm.getDestination());
        log.info("Destination address: {}", destination);
        // Build the order
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
        orderRepository.save(orders);
        user.getOrders().add(orders);
        userRepository.save(user); // Update user's orders
        return OrdersDto.toDto(orders);
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrdersDto.toDto(order);
    }

    @Override
    public List<OrdersDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrdersDto::toDto)
                .toList();
    }

    @Override
    public List<OrdersDto> getMyOrder() {
        User user = this.getCurrentUser();
        return user.getOrders().stream()
                .map(OrdersDto::toDto)
                .toList();
    }

    @Override
    public String deleteOrder(Long id) {
        // Tìm đơn hàng theo id
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Lấy người dùng hiện tại
        User currentUser = this.getCurrentUser();

        // Kiểm tra nếu người dùng hiện tại là chủ đơn hàng hoặc có quyền admin
        if (!order.getUser().equals(currentUser) && !currentUser.getRole().getName().equals("ROLE_ADMIN")) {
            throw new IllegalStateException("You don't have permission to delete this order");
        }

        // Kiểm tra trạng thái đơn hàng
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only orders with PENDING status can be deleted");
        }

        // Xóa tham chiếu đến địa chỉ nếu muốn (không bắt buộc)
        order.setOrigin(null);
        order.setDestination(null);

        // Xóa đơn hàng nhưng giữ lại địa chỉ
        orderRepository.delete(order);

        return "Deleted Order Successfully";
    }



    @Override
    public OrdersDto changeStatusOrder(Long id, OrderStatus status) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status.toString()));
        orderRepository.save(order);
        return OrdersDto.toDto(order);
    }

    private Address createOrGetAddress(AddressForm addressForm) {
        // Trim the address name
        String addressName = addressForm.getName().trim();

        // Get address items
        List<AddressItem> addressItems = convertAddressItems(addressForm);
        if (addressItems.size() != 3) {
            throw new BadRequestException("Address items size mismatch");
        }
        log.info("Address items: {}", addressItems);

        // Check if the address already exists
        Optional<Address> existingAddress = addressRepository.findByNameAndAddressItems(addressName, addressItems);

        // If the address exists and has at least 3 items, return it; otherwise, create a new one
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            // Check if the existing address has at least 3 items
            if (address.getAddressItems().size() >= 3) {
                return address; // Return the existing address if it has enough items
            }
        }

        // Create a new address since the existing one is not suitable
        Address newAddress = Address.builder()
                .name(addressName)
                .addressItems(addressItems)
                .build();
        return addressRepository.save(newAddress); // Save the new address
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
    private User getCurrentUser() {
        return userRepository.findByEmail(UserUtils.getMe()).orElseThrow(() -> new IllegalArgumentException("User not sign in"));
    }
}

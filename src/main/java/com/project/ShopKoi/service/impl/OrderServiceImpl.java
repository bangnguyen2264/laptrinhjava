package com.project.ShopKoi.service.impl;

import com.project.ShopKoi.exception.BadRequestException;
import com.project.ShopKoi.exception.NotFoundException;
import com.project.ShopKoi.exception.UnauthorizationException;
import com.project.ShopKoi.model.dto.OrdersDto;
import com.project.ShopKoi.model.dto.PriceTableDto;
import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.AddressItem;
import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.entity.User;
import com.project.ShopKoi.model.enums.OrderStatus;
import com.project.ShopKoi.model.enums.TransportMethod;
import com.project.ShopKoi.model.form.AddressForm;
import com.project.ShopKoi.model.form.FeedbackForm;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Orders orders = buildOrders(orderForm,origin,destination,user);
        orders.setPrice(ShipFee.calculate(orders));
        orderRepository.save(orders);
        user.getOrders().add(orders);
        userRepository.save(user); // Update user's orders
        return OrdersDto.toDto(orders);
    }

    private Orders buildOrders(OrdersForm ordersForm, Address origin, Address destination, User user) {
        return Orders.builder()
                .orderNumber(UUID.randomUUID())
                .title("Đơn hàng của " + UserUtils.getMe())
                .origin(origin)
                .destination(destination)
                .quantity(ordersForm.getQuantity())
                .weight(ordersForm.getWeight())
                .status(OrderStatus.PENDING)
                .method(ordersForm.getMethod())
                .user(user)
                .note(ordersForm.getNote())
                .build();
    }

    @Override
    public OrdersDto getOrderById(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrdersDto.toDto(order);
    }

    @Override
    public OrdersDto getOrderByOrderNumber(UUID orderNumber) {
        Orders orders = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(()-> new NotFoundException("Order with order number " + orderNumber + " not found"));
        return OrdersDto.toDto(orders);
    }

    @Override
    public List<OrdersDto> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).stream()
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
    public OrdersDto getMyOrderById(Long id) {
        // Lấy thông tin người dùng đã đăng nhập hiện tại
        User currentUser = this.getCurrentUser();
        // Tìm đơn hàng theo ID
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
        // Kiểm tra xem người dùng có phải chủ sở hữu đơn hàng không
        if (!checkCurrentUser(order)) {
            throw new BadRequestException("Order with ID: " + id + " not in your storage");
        }
        // Chuyển đổi Orders entity thành OrdersDto và trả về
        return OrdersDto.toDto(order);
    }


    @Override
    public String deleteOrder(Long id) {
        // Tìm đơn hàng theo id
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if(this.checkAuthorization(order)) {
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
        throw new IllegalArgumentException("User is not authorized to delete this order");
    }



    @Override
    public OrdersDto changeStatusOrder(Long id, OrderStatus status) {

        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!checkDeliver(order)){
            throw new BadRequestException("Order with ID: " + id + " not in your storage");
        }
            order.setStatus(OrderStatus.valueOf(status.toString()));

            orderRepository.save(order);
            return OrdersDto.toDto(order);
    }

    @Override
    public List<PriceTableDto> showPriceTable(OrdersForm ordersForm) {
        // Tạo địa chỉ gốc và địa chỉ đích dựa trên thông tin từ OrdersForm
        Address origin = createOrGetAddress(ordersForm.getOrigin());
        Address destination = createOrGetAddress(ordersForm.getDestination());

        // Tính khoảng cách giữa hai địa chỉ
        double distance = ShipFee.calculateDistance(origin, destination);

        // Lấy các thông tin cần thiết
        int quantity = ordersForm.getQuantity();
        double weight = ordersForm.getWeight();

        // Tạo bảng giá cho từng phương thức vận chuyển
        PriceTableDto airTransport = calculatePriceForMethod(TransportMethod.AIR, distance, quantity);
        PriceTableDto seaTransport = calculatePriceForMethod(TransportMethod.SEA, distance, quantity);
        PriceTableDto landTransport = calculatePriceForMethod(TransportMethod.LAND, distance, quantity);

        // Trả về danh sách bảng giá
        return List.of(airTransport, seaTransport, landTransport);
    }

    @Override
    public void sendFeedback(Long id, FeedbackForm feedbackForm) {

        Orders order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        if (!checkCurrentUser(order)){
            throw new UnauthorizationException("User is not owner the order to send this feedback");
        }
        if (order.getStatus() == OrderStatus.COMPLETED ) {
            order.setRating(feedbackForm.getRating());
            order.setFeedbackMessage(feedbackForm.getFeedbackMessage());
            orderRepository.save(order);
        }
    throw new BadRequestException("The order does not completed");
    }

    @Override
    public List<OrdersDto> getMyDeliverOrder() {
        User user = this.getCurrentUser();
        List<Orders> ordersList = user.getDeliveryOrders();
        return ordersList.stream().map(OrdersDto::toDto).toList();
    }

    @Override
    public void removeOrderFromDelivery(Long id) {
        User user = this.getCurrentUser();
        List<Orders> ordersList = user.getDeliveryOrders();

        // Tìm đơn hàng với ID cụ thể
        Orders orderToRemove = ordersList.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found"));

        // Ngắt kết nối giữa đơn hàng và người giao hàng
        orderToRemove.setDeliver(null);

        // Xóa đơn hàng khỏi danh sách `deliveryOrders` của user hiện tại
        ordersList.remove(orderToRemove);

        // Lưu thay đổi vào cơ sở dữ liệu
        orderRepository.save(orderToRemove);
        userRepository.save(user);
    }




    @Override
    public void assignDelivery(Long orderId, Long deliveryId) {
        User deliver = userRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException("Delivery order not found"));
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getDeliver() == null && !deliver.getDeliveryOrders().contains(order))
        {
            order.setDeliver(deliver);
            orderRepository.save(order);
            deliver.getDeliveryOrders().add(order);
            userRepository.save(deliver);
        }
        else {
            throw new BadRequestException("Delivery order already assigned");
        }
    }

    @Override
    public void updateDelivery(Long orderId, Long deliveryId) {
        // Lấy thông tin nhân viên giao hàng mới
        User newDeliver = userRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery order not found"));

        // Lấy thông tin đơn hàng
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Kiểm tra xem đơn hàng đã được gán cho nhân viên giao hàng nào chưa
        User currentDeliver = order.getDeliver();

        if (currentDeliver != null) {
            // Nếu đơn hàng đã có nhân viên giao hàng cũ, loại bỏ đơn hàng khỏi danh sách của họ
            currentDeliver.getDeliveryOrders().remove(order);
            userRepository.save(currentDeliver); // Cập nhật thông tin nhân viên giao hàng cũ
        }

        // Gán đơn hàng cho nhân viên giao hàng mới
        order.setDeliver(newDeliver);
        orderRepository.save(order);

        newDeliver.getDeliveryOrders().add(order);
        userRepository.save(newDeliver);
    }


    private PriceTableDto calculatePriceForMethod(TransportMethod method, double distance, int quantity) {
        // Tính phí vận chuyển dựa trên phương thức
        double costPerKm = switch (method) {
            case AIR -> ShipFee.AIR_RATE_PER_KM;
            case SEA -> ShipFee.SEA_RATE_PER_KM;
            case LAND -> ShipFee.LAND_RATE_PER_KM;
        };

        // Tính phí cơ bản dựa trên khoảng cách và số lượng
        double baseCost = (distance * costPerKm) * quantity;

        // Tính tổng phí trước VAT
        double totalCostBeforeVAT = baseCost + ShipFee.ADDITIONAL_SERVICE_FEE;

        // Làm tròn tổng phí trước VAT
        totalCostBeforeVAT = Math.floor(totalCostBeforeVAT);

        // Tính VAT (8%)
        double vat = totalCostBeforeVAT * 0.08;

        // Làm tròn VAT
        vat = Math.floor(vat);

        // Tổng phí bao gồm VAT
        double totalCost = totalCostBeforeVAT + vat;

        // Trả về bảng giá
        return PriceTableDto.builder()
                .method(method)
                .price(totalCost)
                .build();
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

    private boolean checkAuthorization(Orders order) {
        // Lấy người dùng hiện tại
        User currentUser = this.getCurrentUser();
        return currentUser.getRole().getName().equals("ROLE_ADMIN") || currentUser.getRole().getName().equals("ROLE_DELIVER");
    }
    private boolean checkCurrentUser(Orders order) {
        User currentUser = this.getCurrentUser();
        return order.getUser().equals(currentUser) ;
    }
    private boolean checkDeliver(Orders orders){
        User currentUser = this.getCurrentUser();
        return orders.getDeliver().equals(currentUser);
    }
}

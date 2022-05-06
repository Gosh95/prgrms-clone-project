package prgrms.project.starbooks.service;

import prgrms.project.starbooks.controller.order.OrderCreateRequest;
import prgrms.project.starbooks.controller.order.OrderDetailResponse;
import prgrms.project.starbooks.controller.order.OrderResponse;
import prgrms.project.starbooks.controller.order.OrderUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);

    OrderResponse searchById(UUID orderId);

    List<OrderDetailResponse> searchOrderDetails(UUID customerId);

    List<OrderResponse> searchAllOrders();

    OrderResponse updateOrder(UUID orderId, OrderUpdateRequest orderUpdateRequest);

    void deleteById(UUID orderId);

    void deleteAllOrders();
}

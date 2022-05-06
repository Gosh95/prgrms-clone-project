package prgrms.project.starbooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prgrms.project.starbooks.controller.order.OrderCreateRequest;
import prgrms.project.starbooks.controller.order.OrderDetailResponse;
import prgrms.project.starbooks.controller.order.OrderResponse;
import prgrms.project.starbooks.controller.order.OrderUpdateRequest;
import prgrms.project.starbooks.repository.CustomerRepository;
import prgrms.project.starbooks.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static prgrms.project.starbooks.controller.order.OrderResponse.orderToResponse;
import static prgrms.project.starbooks.util.exception.ErrorMessage.CANT_FIND_CUSTOMER;
import static prgrms.project.starbooks.util.exception.ErrorMessage.CANT_FIND_ORDER;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        var customer = customerRepository.findById(orderCreateRequest.customerId()).orElseThrow(() -> new NoSuchElementException(CANT_FIND_CUSTOMER.getMessage()));
        var order = orderCreateRequest.requestToOrder(customer);
        var newOrder = order.doOrder(orderCreateRequest.discountPolicy());
        var savedOrder = orderRepository.save(newOrder);

        return orderToResponse(savedOrder);
    }

    @Override
    public OrderResponse searchById(UUID orderId) {
        var retrievedOrder = orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_ORDER.getMessage()));

        return orderToResponse(retrievedOrder);
    }

    @Override
    public List<OrderDetailResponse> searchOrderDetails(UUID customerId) {
        return orderRepository.findOrderDetails(customerId).stream().map(OrderDetailResponse::of).toList();
    }

    @Override
    public List<OrderResponse> searchAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponse::orderToResponse).toList();
    }

    @Override
    public OrderResponse updateOrder(UUID orderId, OrderUpdateRequest orderUpdateRequest) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_ORDER.getMessage()));
        var updatedOrder = order.update(orderUpdateRequest.orderStatus());

        var retrievedOrder = orderRepository.update(updatedOrder);

        return orderToResponse(retrievedOrder);
    }

    @Override
    public void deleteById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}

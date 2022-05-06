package prgrms.project.starbooks.controller.order;

import lombok.Builder;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.order.Order;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.order.OrderStatus;
import prgrms.project.starbooks.domain.order.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Customer customer,
        List<OrderItem> orderItems,
        OrderStatus orderStatus,
        PaymentMethod paymentMethod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @Builder
    public OrderResponse {
    }

    public static OrderResponse orderToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customer(order.getCustomer())
                .orderItems(order.getOrderItems())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}

package prgrms.project.starbooks.controller.order;

import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.OrderDetail;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.order.OrderStatus;

import java.util.List;
import java.util.UUID;

public record OrderDetailResponse(
        UUID orderId,
        String customerName,
        Address address,
        List<OrderItem> orderItems,
        OrderStatus orderStatus
) {

    public static OrderDetailResponse of(OrderDetail orderDetail) {
        return new OrderDetailResponse(orderDetail.orderId(), orderDetail.customerName(), orderDetail.address(), orderDetail.orderItems(), orderDetail.orderStatus());
    }
}

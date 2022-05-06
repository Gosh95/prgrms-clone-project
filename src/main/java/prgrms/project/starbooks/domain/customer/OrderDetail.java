package prgrms.project.starbooks.domain.customer;

import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.order.OrderStatus;

import java.util.List;
import java.util.UUID;

public record OrderDetail(
        UUID orderId,
        String customerName,
        Address address,
        List<OrderItem> orderItems,
        OrderStatus orderStatus
) {

}

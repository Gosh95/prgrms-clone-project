package prgrms.project.starbooks.controller.order;

import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.order.DiscountPolicy;
import prgrms.project.starbooks.domain.order.Order;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.order.PaymentMethod;

import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static prgrms.project.starbooks.domain.order.OrderStatus.ACCEPTED;

public record OrderCreateRequest(
        UUID customerId,
        List<OrderItem> orderItems,
        PaymentMethod paymentMethod,
        DiscountPolicy discountPolicy
) {

    public Order requestToOrder(Customer customer) {
        return Order.builder()
                .orderId(randomUUID())
                .customer(customer)
                .orderItems(this.orderItems())
                .orderStatus(ACCEPTED)
                .paymentMethod(this.paymentMethod())
                .createdAt(now())
                .updatedAt(now())
                .build();
    }
}

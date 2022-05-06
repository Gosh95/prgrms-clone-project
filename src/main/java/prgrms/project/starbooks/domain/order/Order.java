package prgrms.project.starbooks.domain.order;

import lombok.Builder;
import lombok.Getter;
import prgrms.project.starbooks.domain.customer.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static prgrms.project.starbooks.domain.order.OrderStatus.ACCEPTED;

@Getter
public class Order {

    private final UUID orderId;
    private final Customer customer;
    private final List<OrderItem> orderItems;
    private OrderStatus orderStatus;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(UUID orderId, Customer customer, List<OrderItem> orderItems, PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = orderItems;
        this.orderStatus = ACCEPTED;
        this.paymentMethod = paymentMethod;
        createdAt = now();
        updatedAt = now();
    }

    @Builder
    public Order(UUID orderId, Customer customer, List<OrderItem> orderItems, OrderStatus orderStatus, PaymentMethod paymentMethod, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order doOrder(DiscountPolicy discountPolicy) {
        var afterDiscount = discountPolicy.discount(getTotalPrice());
        this.customer.makeAPayment(afterDiscount);

        return this;
    }

    public Order update(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updatedAt = now();

        return this;
    }

    public long getTotalPrice() {
        return this.orderItems.stream().mapToLong(i -> i.price() * i.quantity()).sum();
    }
}

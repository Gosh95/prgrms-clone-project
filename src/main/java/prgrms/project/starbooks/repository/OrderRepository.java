package prgrms.project.starbooks.repository;

import prgrms.project.starbooks.domain.customer.OrderDetail;
import prgrms.project.starbooks.domain.order.Order;
import prgrms.project.starbooks.domain.order.OrderItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    List<OrderItem> findOrderItems(UUID orderId);

    List<OrderDetail> findOrderDetails(UUID customerID);

    List<Order> findAll();

    Order update(Order order);

    void deleteById(UUID orderId);

    void deleteAll();
}

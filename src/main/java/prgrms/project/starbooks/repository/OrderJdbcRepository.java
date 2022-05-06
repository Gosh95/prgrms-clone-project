package prgrms.project.starbooks.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import prgrms.project.starbooks.domain.customer.*;
import prgrms.project.starbooks.domain.order.Order;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.order.OrderStatus;
import prgrms.project.starbooks.domain.order.PaymentMethod;
import prgrms.project.starbooks.domain.product.Category;
import prgrms.project.starbooks.util.exception.DataUnchangedException;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.singletonMap;
import static prgrms.project.starbooks.util.MyObjectMapper.convertToMap;
import static prgrms.project.starbooks.util.Utils.toLocalDateTime;
import static prgrms.project.starbooks.util.Utils.toUUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Order save(Order order) {
        jdbcTemplate.update(
                """
                        INSERT INTO orders(order_id, order_status, payment_method, created_at, updated_at)
                         VALUES (UUID_TO_BIN(:orderId), :orderStatus, :paymentMethod, :createdAt, :updatedAt)
                        """,
                toOrderParamMap(order)
        );

        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update(
                                """
                                        INSERT INTO order_items(order_id, product_id, product_name, category, price, quantity, created_at, updated_at)
                                         VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :productName, :category, :price, :quantity, :createdAt, :updatedAt)
                                        """,
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item))
                );

        jdbcTemplate.update(
                """
                        INSERT INTO order_details(order_id, customer_id, customer_name, city, zipcode, order_status, created_at, updated_at)
                         VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:customerId), :customerName, :city, :zipcode, :orderStatus, :createdAt, :updatedAt)
                        """,
                toOrderDetailParamMap(order, order.getCustomer())
        );

        jdbcTemplate.update(
                """
                        UPDATE customers Set money = :money, updated_at = :updatedAt
                         WHERE customer_id = UUID_TO_BIN(:customerId)
                        """,
                toCustomerParamMap(order.getCustomer())
        );

        return order;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                        """
                                SELECT * FROM customers, orders
                                WHERE customer_id = (SELECT customer_id FROM order_details WHERE order_id = UUID_TO_BIN(:orderId))
                                AND order_id = UUID_TO_BIN(:orderId)
                                """,
                            singletonMap("orderId", orderId.toString().getBytes()),
                            orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            log.error("Got empty result: {}", e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public List<OrderItem> findOrderItems(UUID orderId) {
        return jdbcTemplate.query(
                """
                        SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)
                        """,
                singletonMap("orderId", orderId.toString().getBytes()),
                orderItemRowMapper
        );
    }

    @Override
    public List<OrderDetail> findOrderDetails(UUID customerId) {
        return jdbcTemplate.query(
                """
                  SELECT * FROM order_details WHERE customer_id = UUID_TO_BIN(:customerId)
                  """,
                singletonMap("customerId", customerId.toString().getBytes()),
                orderDetailRowMapper
        );
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT * from orders, customers
                        WHERE customer_id = (SELECT customer_id from order_details where orders.order_id = order_details.order_id)
                        """,
                orderRowMapper
        );
    }

    @Override
    public Order update(Order order) {
        var updated = jdbcTemplate.update(
                """
                        UPDATE orders o, order_details d SET o.order_status = :orderStatus, d.order_status = :orderStatus, o.updated_at = :updatedAt, d.updated_at = :updatedAt
                         WHERE o.order_id = UUID_TO_BIN(:orderId) AND d.order_id = UUID_TO_BIN(:orderId)
                        """,
                toOrderParamMap(order)
        );

        if (updated < 1) {
            log.error("Got different result than expected: {}", updated);

            throw new DataUnchangedException("Nothing was updated.");
        }

        return order;
    }

    @Override
    public void deleteById(UUID orderId) {
        jdbcTemplate.update(
                "DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                singletonMap("orderId", orderId.toString().getBytes())
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        return convertToMap(order);
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem orderItem) {
        var paramMap = new HashMap<String, Object>();

        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", orderItem.productId().toString().getBytes());
        paramMap.put("productName", orderItem.productName());
        paramMap.put("category", orderItem.category().toString());
        paramMap.put("price", orderItem.price());
        paramMap.put("quantity", orderItem.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);

        return paramMap;
    }

    private Map<String, Object> toOrderDetailParamMap(Order order, Customer customer) {
        var paramMap = new HashMap<String, Object>();

        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("customerId", customer.getCustomerId().toString().getBytes());
        paramMap.put("customerName", customer.getCustomerName());
        paramMap.put("city", customer.getAddress().city());
        paramMap.put("zipcode", customer.getAddress().zipcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());

        return paramMap;
    }

    private Map<String, Object> toCustomerParamMap(Customer customer) {
        var paramMap = new HashMap<String, Object>();

        paramMap.put("customerId", customer.getCustomerId().toString().getBytes());
        paramMap.put("money", customer.getWallet().getMoney());
        paramMap.put("updatedAt", customer.getUpdatedAt());

        return paramMap;
    }

    private final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var paymentMethod = PaymentMethod.valueOf(resultSet.getString("payment_method"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("orders.created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("orders.updated_at"));

        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var customerName = resultSet.getString("customer_name");
        var email = new Email(resultSet.getString("email"));
        var city = resultSet.getString("city");
        var zipcode = resultSet.getString("zipcode");
        var money = resultSet.getLong("money");
        var customerCreatedAt = toLocalDateTime(resultSet.getTimestamp("customers.created_at"));
        var customerUpdatedAt = toLocalDateTime(resultSet.getTimestamp("customers.updated_at"));

        return Order.builder()
                .orderId(orderId)
                .customer(new Customer(customerId, customerName, email, new Address(city, zipcode), new Wallet(money), customerCreatedAt, customerUpdatedAt))
                .orderItems(findOrderItems(orderId))
                .orderStatus(orderStatus)
                .paymentMethod(paymentMethod)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    };

    private final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");

        return new OrderItem(productId, productName, category, price, quantity);
    };

    private final RowMapper<OrderDetail> orderDetailRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var customerName = resultSet.getString("customer_name");
        var city = resultSet.getString("city");
        var zipcode = resultSet.getString("zipcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));

        return new OrderDetail(orderId, customerName, new Address(city, zipcode), findOrderItems(orderId), orderStatus);
    };
}

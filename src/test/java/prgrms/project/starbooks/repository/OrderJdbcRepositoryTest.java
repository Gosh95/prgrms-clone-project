package prgrms.project.starbooks.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;
import prgrms.project.starbooks.domain.order.Order;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.product.Product;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static prgrms.project.starbooks.domain.order.OrderStatus.READY_FOR_DELIVERY;
import static prgrms.project.starbooks.domain.order.PaymentMethod.CARD;
import static prgrms.project.starbooks.domain.product.Category.ECONOMY;
import static prgrms.project.starbooks.domain.product.Category.IT;

class OrderJdbcRepositoryTest extends TestConfig {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    List<OrderItem> newOrderItems;

    Order newOrder;

    @BeforeEach
    void setup() {
        var customer = new Customer(randomUUID(), "test-customer", new Email("test@gmail.com"), new Address("seoul", "12345"), new Wallet(1000L), now(), now());

        customerRepository.save(customer);

        var product1 = new Product(randomUUID(), "test-product1", IT, 1000L, "this is test IT book.", now(), now());
        var product2 = new Product(randomUUID(), "test-product2", ECONOMY, 2000L, "this is test ECONOMY book.", now(), now());

        productRepository.save(product1);
        productRepository.save(product2);

        var orderItem1 = new OrderItem(product1.getProductId(), product1.getProductName(), product1.getCategory(), product1.getPrice(), 10);
        var orderItem2 = new OrderItem(product1.getProductId(), product1.getProductName(), product1.getCategory(), product1.getPrice(), 10);

        newOrderItems = List.of(orderItem1, orderItem2);

        newOrder = new Order(randomUUID(), customer, newOrderItems, CARD);
    }

    @AfterEach
    void cleanup() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Order 를 저장할 수 있다.")
    void testSave() {
        var savedOrder = orderRepository.save(newOrder);

        assertThat(savedOrder, notNullValue());
        assertThat(savedOrder, samePropertyValuesAs(newOrder));
        assertThat(savedOrder.getOrderItems(), samePropertyValuesAs(newOrderItems));
    }

    @Test
    @DisplayName("중복된 Order 는 예외가 발생해야 한다.")
    void testDuplicatedSave() {
        var savedOrder = orderRepository.save(newOrder);

        assertThrows(DataAccessException.class, () -> orderRepository.save(savedOrder));
    }

    @Test
    @DisplayName("OrderId 로 Order 를 찾을 수 있다.")
    void testFindById() {
        var savedOrder = orderRepository.save(newOrder);
        var retrievedOrder = orderRepository.findById(savedOrder.getOrderId());

        assertThat(retrievedOrder.isEmpty(), is(false));
        assertThat(retrievedOrder.get().getOrderId(), is(savedOrder.getOrderId()));
        assertThat(retrievedOrder.get().getOrderItems(), is(newOrderItems));
    }

    @Test
    @DisplayName("모든 Order 를 찾을 수 있다.")
    void testFindAll() {
        var savedOrder = orderRepository.save(newOrder);
        var retrievedOrders = orderRepository.findAll();

        assertThat(retrievedOrders.size(), is(1));
        assertThat(retrievedOrders.get(0), samePropertyValuesAs(savedOrder));
    }

    @Test
    @DisplayName("Order 를 업데이트할 수 있다.")
    void testUpdate() {
        var beforeUpdate = newOrder.getUpdatedAt();

        var savedOrder = orderRepository.save(newOrder);
        var updatedOrder = savedOrder.update(READY_FOR_DELIVERY);
        var retrievedOrder = orderRepository.update(updatedOrder);

        assertThat(retrievedOrder, notNullValue());
        assertThat(retrievedOrder.getOrderStatus(), is(READY_FOR_DELIVERY));
        assertThat(retrievedOrder.getUpdatedAt(), greaterThan(beforeUpdate));
    }

    @Test
    @DisplayName("Order 를 삭제할 수 있다.")
    void testDeleteById() {
        var savedOrder = orderRepository.save(newOrder);
        var beforeDelete = orderRepository.findById(savedOrder.getOrderId());

        assertThat(beforeDelete.isEmpty(), is(false));

        orderRepository.deleteById(savedOrder.getOrderId());
        var afterDelete = orderRepository.findById(savedOrder.getOrderId());

        assertThat(afterDelete.isEmpty(), is(true));
    }

    @Test
    @DisplayName("모든 Order 를 삭제할 수 있다.")
    void testDeleteAll() {
        orderRepository.save(newOrder);

        assertThat(orderRepository.findAll().size(), is(1));

        orderRepository.deleteAll();

        assertThat(orderRepository.findAll().size(), is(0));
    }
}
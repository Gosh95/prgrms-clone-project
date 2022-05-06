package prgrms.project.starbooks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.controller.order.OrderCreateRequest;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;
import prgrms.project.starbooks.domain.order.OrderItem;
import prgrms.project.starbooks.domain.product.Product;
import prgrms.project.starbooks.repository.CustomerRepository;
import prgrms.project.starbooks.repository.ProductRepository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static prgrms.project.starbooks.domain.order.DiscountPolicy.FIXED;
import static prgrms.project.starbooks.domain.order.PaymentMethod.CARD;
import static prgrms.project.starbooks.domain.product.Category.IT;

class DefaultOrderServiceTest extends TestConfig {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DefaultOrderService orderService;

    @BeforeEach
    void cleanup() {
        productRepository.deleteAll();
        customerRepository.deleteAll();
        orderService.deleteAllOrders();
    }

    @Test
    @DisplayName("총 주문 금액이 고객이 보유한 돈 보다 클 경우 예외가 발생한다.")
    void testTotalPriceGreaterThanMoney() {
        var customer = new Customer(randomUUID(), "test-customer", new Email("test@gmail.com"), new Address("city", "12345"), new Wallet(10000L), now(), now());
        customerRepository.save(customer);

        var product1 = new Product(randomUUID(), "product1", IT, 1000L);
        var product2 = new Product(randomUUID(), "product1", IT, 2000L);
        productRepository.save(product1);
        productRepository.save(product2);

        var orderItem1 = new OrderItem(product1.getProductId(), product1.getProductName(), product1.getCategory(), product1.getPrice(), 10000);
        var orderItem2 = new OrderItem(product2.getProductId(), product2.getProductName(), product2.getCategory(), product2.getPrice(), 10000);

        var orderRequest = new OrderCreateRequest(customer.getCustomerId(), List.of(orderItem1, orderItem2), CARD, FIXED);

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    @DisplayName("고객이 보유한 금액이 총 주문 금액보다 클 경우 주문이 정상적으로 생성되고 고객의 보유 금액은 총 주문 금액 만큼 차감된다.")
    void testMoneyGreaterThanTotalPrice() {
        var customer = new Customer(randomUUID(), "test-customer", new Email("test@gmail.com"), new Address("city", "12345"), new Wallet(999999999L), now(), now());
        customerRepository.save(customer);

        var product1 = new Product(randomUUID(), "product1", IT, 1000L);
        var product2 = new Product(randomUUID(), "product1", IT, 2000L);
        productRepository.save(product1);
        productRepository.save(product2);

        var orderItem1 = new OrderItem(product1.getProductId(), product1.getProductName(), product1.getCategory(), product1.getPrice(), 100);
        var orderItem2 = new OrderItem(product2.getProductId(), product2.getProductName(), product2.getCategory(), product2.getPrice(), 100);
        List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

        var orderRequest = new OrderCreateRequest(customer.getCustomerId(), orderItems, CARD, FIXED);

        long expectedResult = customer.getWallet().getMoney() - orderRequest.discountPolicy().discount(orderItems.stream().mapToLong(i -> i.price() * i.quantity()).sum());

        var orderResponse = orderService.createOrder(orderRequest);

        assertThat(orderResponse.customer().getCustomerId()).isEqualTo(customer.getCustomerId());
        assertThat(orderResponse.customer().getWallet().getMoney()).isEqualTo(expectedResult);
    }
}
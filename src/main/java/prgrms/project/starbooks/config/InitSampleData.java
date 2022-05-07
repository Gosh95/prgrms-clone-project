package prgrms.project.starbooks.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;
import prgrms.project.starbooks.domain.product.Product;
import prgrms.project.starbooks.repository.CustomerRepository;
import prgrms.project.starbooks.repository.OrderRepository;
import prgrms.project.starbooks.repository.ProductRepository;

import javax.annotation.PostConstruct;

import static java.util.UUID.randomUUID;
import static prgrms.project.starbooks.domain.product.Category.IT;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitSampleData {
    private final InitService initService;

    @PostConstruct
    void initService() {
        initService.addInitData();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

        void addInitData() {
            orderRepository.deleteAll();
            customerRepository.deleteAll();
            productRepository.deleteAll();

            for(int i = 0; i < 10; i++) {
                var customer = new Customer(randomUUID(), "customer" + i, new Email("test" + i + "@gmail.com"), new Address("seoul", "12345"), new Wallet(100000L));
                customerRepository.save(customer);

                var product = new Product(randomUUID(), "java" + i, IT, 10000L);
                productRepository.save(product);
            }
        }
    }
}

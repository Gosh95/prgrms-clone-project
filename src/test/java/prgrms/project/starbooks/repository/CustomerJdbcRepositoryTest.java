package prgrms.project.starbooks.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerJdbcRepositoryTest extends TestConfig {

    @Autowired
    CustomerRepository customerRepository;

    Customer newCustomer = Customer.builder()
            .customerId(randomUUID())
            .customerName("test-customer")
            .email(new Email("test@gmail.com"))
            .address(new Address("seoul", "12345"))
            .wallet(new Wallet())
            .createdAt(now())
            .updatedAt(now())
            .build();


    static List<Customer> getCustomers() {
        return List.of(
                Customer.builder().customerId(randomUUID()).customerName("test-customer1").email(new Email("test1@gmail.com")).address(new Address("seoul", "12345")).wallet(new Wallet()).createdAt(now()).updatedAt(now()).build(),
                Customer.builder().customerId(randomUUID()).customerName("test-customer2").email(new Email("test2@gmail.com")).address(new Address("incheon", "12342")).wallet(new Wallet()).createdAt(now()).updatedAt(now()).build(),
                Customer.builder().customerId(randomUUID()).customerName("test-customer3").email(new Email("test3@gmail.com")).address(new Address("daegu", "52241")).wallet(new Wallet()).createdAt(now()).updatedAt(now()).build(),
                Customer.builder().customerId(randomUUID()).customerName("test-customer4").email(new Email("test4@gmail.com")).address(new Address("busan", "45512")).wallet(new Wallet()).createdAt(now()).updatedAt(now()).build(),
                Customer.builder().customerId(randomUUID()).customerName("test-customer5").email(new Email("test5@gmail.com")).address(new Address("jeju", "46561")).wallet(new Wallet()).createdAt(now()).updatedAt(now()).build()
        );
    }

    @BeforeEach
    void cleanup() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Customer 를 저장할 수 있다.")
    void testSave() {
        var savedCustomer = customerRepository.save(newCustomer);

        assertThat(savedCustomer, notNullValue());
        assertThat(savedCustomer, is(newCustomer));
    }

    @Test
    @DisplayName("중복된 Customer 는 예외가 발생해야 한다.")
    void testDuplicatedCustomer() {
        customerRepository.save(newCustomer);

        assertThrows(DataAccessException.class, () -> customerRepository.save(newCustomer));
    }

    @Test
    @DisplayName("중복된 email 을 가진 Customer 는 예외가 발생해야 한다.")
    void testDuplicatedEmail() {
        var savedCustomer = customerRepository.save(newCustomer);
        var sampleCustomer = new Customer(randomUUID(), "sample-customer", new Email(savedCustomer.getEmail().address()), new Address("city", "12345"), new Wallet());

        assertThrows(DuplicateKeyException.class, () -> customerRepository.save(sampleCustomer));
    }

    @Test
    @DisplayName("CustomerId 로 Customer 를 찾을 수 있다.")
    void testFindById() {
        var savedCustomer = customerRepository.save(newCustomer);
        var retrievedCustomer = customerRepository.findById(savedCustomer.getCustomerId());

        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(savedCustomer));
    }

    @Test
    @DisplayName("모든 Customer 를 찾을 수 있다.")
    void testFindAll() {
        var customers = getCustomers();
        customers.forEach(customerRepository::save);
        var retrievedCustomers = customerRepository.findAll();

        assertThat(retrievedCustomers.size(), is(5));
    }

    @Test
    @DisplayName("Customer 를 업데이트할 수 있다.")
    void testUpdate() {
        var newCustomerName = "updated-customer";
        var newAddress = new Address("newCity", "99999");
        var newWallet = new Wallet(99999L);
        var beforeUpdate = newCustomer.getUpdatedAt();

        var savedCustomer = customerRepository.save(newCustomer);
        var updatedCustomer = savedCustomer.update(newCustomerName, newAddress, newWallet);
        var retrievedCustomer = customerRepository.update(updatedCustomer);

        assertThat(retrievedCustomer, notNullValue());
        assertThat(retrievedCustomer.getCustomerName(), is(newCustomerName));
        assertThat(retrievedCustomer.getAddress().zipcode(), is("99999"));
        assertThat(retrievedCustomer.getWallet().getMoney(), is(99999L));
        assertThat(retrievedCustomer.getUpdatedAt(), greaterThan(beforeUpdate));
    }

    @Test
    @DisplayName("Customer 를 삭제할 수 있다.")
    void testDeleteById() {
        var savedCustomer = customerRepository.save(newCustomer);
        var beforeDelete = customerRepository.findById(savedCustomer.getCustomerId());

        assertThat(beforeDelete.isEmpty(), is(false));

        customerRepository.deleteById(savedCustomer.getCustomerId());
        var afterDelete = customerRepository.findById(savedCustomer.getCustomerId());

        assertThat(afterDelete.isEmpty(), is(true));
    }

    @Test
    @DisplayName("모든 Customer 를 삭제할 수 있다.")
    void testDeleteAll() {
        var customers = getCustomers();
        customers.forEach(customerRepository::save);

        assertThat(customerRepository.findAll().size(), is(5));

        customerRepository.deleteAll();

        assertThat(customerRepository.findAll().size(), is(0));
    }
}
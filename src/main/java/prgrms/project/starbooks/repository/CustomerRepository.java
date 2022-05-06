package prgrms.project.starbooks.repository;

import prgrms.project.starbooks.domain.customer.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID customerId);

    List<Customer> findAll();

    Customer update(Customer customer);

    void deleteById(UUID customerId);

    void deleteAll();
}

package prgrms.project.starbooks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prgrms.project.starbooks.controller.customer.CustomerRequest;
import prgrms.project.starbooks.controller.customer.CustomerResponse;
import prgrms.project.starbooks.repository.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static prgrms.project.starbooks.controller.customer.CustomerResponse.customerToResponse;
import static prgrms.project.starbooks.util.exception.ErrorMessage.CANT_FIND_CUSTOMER;

@Service
@RequiredArgsConstructor
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse registerCustomer(CustomerRequest customerRequest) {
        var customer =  customerRequest.requestToCustomer();
        var savedCustomer = customerRepository.save(customer);

        return customerToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse searchById(UUID customerId) {
        var retrievedCustomer =  customerRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_CUSTOMER.getMessage()));

        return customerToResponse(retrievedCustomer);
    }

    @Override
    public List<CustomerResponse> searchAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerResponse::customerToResponse).toList();
    }

    @Override
    public CustomerResponse updateCustomer(UUID customerId, CustomerRequest customerRequest) {
        var customer = customerRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_CUSTOMER.getMessage()));
        var updatedCustomer = customer.update(customerRequest.customerName(), customerRequest.address(), customerRequest.wallet());
        var retrievedCustomer = customerRepository.update(updatedCustomer);

        return customerToResponse(retrievedCustomer);
    }

    @Override
    public void deleteById(UUID customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }
}

package prgrms.project.starbooks.service;

import prgrms.project.starbooks.controller.customer.CustomerRequest;
import prgrms.project.starbooks.controller.customer.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse registerCustomer(CustomerRequest customerRequest);

    CustomerResponse searchById(UUID customerId);

    List<CustomerResponse> searchAllCustomers();

    CustomerResponse updateCustomer(UUID customerId, CustomerRequest customerRequest);

    void deleteById(UUID customerId);

    void deleteAllCustomers();
}

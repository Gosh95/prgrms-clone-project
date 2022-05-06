package prgrms.project.starbooks.controller.customer;

import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

public record CustomerRequest(
        String customerName,
        Email email,
        Address address,
        Wallet wallet
) {

    public Customer requestToCustomer() {
        return Customer.builder()
                .customerId(randomUUID())
                .customerName(this.customerName())
                .email(this.email())
                .address(this.address())
                .wallet(this.wallet())
                .createdAt(now())
                .updatedAt(now())
                .build();
    }

}

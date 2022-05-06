package prgrms.project.starbooks.controller.customer;

import lombok.Builder;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID customerId,
        String customerName,
        Email email,
        Address address,
        Wallet wallet,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @Builder
    public CustomerResponse {
    }

    public static CustomerResponse customerToResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .wallet(customer.getWallet())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}

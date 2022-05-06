package prgrms.project.starbooks.domain.customer;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
public class Customer {

    private final UUID customerId;
    private String customerName;
    private final Email email;
    private Address address;
    private Wallet wallet;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Customer(UUID customerId, String customerName, Email email, Address address, Wallet wallet) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.wallet = wallet;
        this.createdAt = now();
        this.updatedAt = now();
    }

    @Builder
    public Customer(UUID customerId, String customerName, Email email, Address address, Wallet wallet, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.wallet = wallet;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Customer update(String customerName, Address address, Wallet wallet) {
        this.customerName = customerName;
        this.address = address;
        this.wallet = wallet;
        this.updatedAt = now();

        return this;
    }

    public void makeAPayment(long price) {
        Assert.isTrue(price <= this.wallet.getMoney(), "Don't have enough money");

        var leftMoney = this.wallet.getMoney() - price;
        this.wallet.updateMoney(leftMoney);
        this.updatedAt = now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getCustomerId(), customer.getCustomerId()) && Objects.equals(getEmail(), customer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getEmail());
    }
}

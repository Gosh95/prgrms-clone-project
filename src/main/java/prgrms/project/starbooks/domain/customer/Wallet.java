package prgrms.project.starbooks.domain.customer;

import org.springframework.util.Assert;

import java.util.Objects;

public class Wallet {

    private long money;

    public Wallet() {
        this.money = 0L;
    }

    public Wallet(long money) {
        validateMoney(money);

        this.money = money;
    }

    public long getMoney() {
        return money;
    }

    public void updateMoney(long money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return getMoney() == wallet.getMoney();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMoney());
    }

    private void validateMoney(long money) {
        Assert.isTrue(money >= 0, "Money must be greater than 0");
    }
}

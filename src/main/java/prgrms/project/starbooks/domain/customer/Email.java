package prgrms.project.starbooks.domain.customer;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String address) {

    private static final Pattern isEmail = Pattern.compile("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b");

    public Email {
        validateAddress(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    private void validateAddress(String address) {
        Assert.notNull(address, "address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50, "address length must be between 4 and 50 characters.");
        Assert.isTrue(isEmail.matcher(address).matches(), "Invalid email address");
    }
}

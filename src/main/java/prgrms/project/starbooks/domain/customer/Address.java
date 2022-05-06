package prgrms.project.starbooks.domain.customer;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

public record Address(String city, String zipcode) {

    private static final Pattern isZipcode = Pattern.compile("[0-9]{5}(-[0-9]{4})?");

    public Address {
        validateZipcode(zipcode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, zipcode);
    }

    private void validateZipcode(String zipcode) {
        Assert.isTrue(isZipcode.matcher(zipcode).matches(), "Invalid zipcode value");
    }
}

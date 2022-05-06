package prgrms.project.starbooks.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {

    @ParameterizedTest
    @CsvSource({"seoul,123123", "jeju,abc", "busan,abc-123"})
    @DisplayName("형식에 맞지 않는 zipcode 가 입력될 경우 예외가 발생한다.")
    void testInvalidZipcode(ArgumentsAccessor accessor) {
        var city = accessor.getString(0);
        var zipcode = accessor.getString(1);

        assertThrows(IllegalArgumentException.class, () -> new Address(city, zipcode));
    }

    @ParameterizedTest
    @CsvSource({"seoul,75631", "jeju,34512", "busan,12345"})
    @DisplayName("Address 값 객체가 생성된다.")
    void testNewAddress(ArgumentsAccessor accessor) {
        var city = accessor.getString(0);
        var zipcode = accessor.getString(1);

        var address = new Address(city, zipcode);

        assertThat(address).isNotNull();
        assertThat(address.city()).isEqualTo(city);
        assertThat(address.zipcode()).isEqualTo(zipcode);
    }
}
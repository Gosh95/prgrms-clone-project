package prgrms.project.starbooks.domain.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, -1000, -123124})
    @DisplayName("money 입력 값이 0보다 작을 경우 예외가 발생한다.")
    void testInvalidMoney(long input) {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(input));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1234, 123523})
    @DisplayName("Wallet 값 객체가 생성된다.")
    void testNewWallet(long input) {
        var wallet = new Wallet(input);

        Assertions.assertThat(wallet.getMoney()).isEqualTo(input);
    }
}
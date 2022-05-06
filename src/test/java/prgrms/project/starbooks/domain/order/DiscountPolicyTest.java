package prgrms.project.starbooks.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.project.starbooks.domain.order.DiscountPolicy.*;

class DiscountPolicyTest {

    @ParameterizedTest
    @ValueSource(longs = {1000L, 2000L, 3000L})
    @DisplayName("할인 선택을 하지 않으면 할인이 적용되지 않는다.")
    void testNoChoiceDiscountPolicy(long input) {
        var afterDiscount = NO_CHOICE.discount(input);

        assertThat(afterDiscount, is(input));
    }

    //FIXED 할인 금액은 2000원으로 고정.
    @ParameterizedTest
    @ValueSource(longs = {1000L, 500L, 100L})
    @DisplayName("고정 할인 금액보다 작은 금액이 입력될 경우 예외가 발생한다.")
    void testInvalidFixedDiscountPolicy(long input) {
        assertThrows(IllegalArgumentException.class, () -> FIXED.discount(input));
    }

    @ParameterizedTest
    @ValueSource(longs = {10000L, 2000L, 9999L})
    @DisplayName("할인 금액 이상의 금액이 입력될 경우 할인이 된다.")
    void testValidFixedDiscountPolicy(long input) {
        var afterDiscount = FIXED.discount(input);

        assertThat(afterDiscount, is(input - 2000L));
    }

    //PERCENT 할인율은 10으로 고정.
    @ParameterizedTest
    @ValueSource(longs = {10000L, 2000L, 9999L})
    @DisplayName("할인율에 맞게 할인이 된다.")
    void testValidPercentDiscountPolicy(long input) {
        var afterDiscount = PERCENT.discount(input);

        assertThat(afterDiscount, is(input - (int) (input * (10 / 100f))));
    }
}
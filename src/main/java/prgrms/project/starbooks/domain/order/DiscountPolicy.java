package prgrms.project.starbooks.domain.order;

import org.springframework.util.Assert;

public enum DiscountPolicy {

    NO_CHOICE {
        @Override
        public long discount(long price) {
            return price;
        }
    },
    FIXED {
         private static final long discountAmount = 2000L;

        @Override
        public long discount(long price) {
            validateDiscountPolicy(price, discountAmount);

            return price - discountAmount;
        }

        public void validateDiscountPolicy(long price, long discountInfo) {
            Assert.isTrue(price >= discountInfo && discountInfo >= 0, "DiscountAmount must be between 0 and price.");
        }
    },
    PERCENT {
        private static final long discountPercent = 10L;

        @Override
        public long discount(long price) {
            validateDiscountPolicy(price, discountPercent);

            return price - (int) (price * (discountPercent / 100f));
        }

        public void validateDiscountPolicy(long price, long discountInfo) {
            Assert.isTrue(0 <= discountInfo && discountInfo <= 100, "DiscountPercent must be between 0 and 100.");
        }
    };

    public abstract long discount(long price);
}

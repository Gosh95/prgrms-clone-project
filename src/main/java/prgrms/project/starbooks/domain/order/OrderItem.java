package prgrms.project.starbooks.domain.order;

import prgrms.project.starbooks.domain.product.Category;

import java.util.UUID;

public record OrderItem(
        UUID productId,
        String productName,
        Category category,
        long price,
        int quantity
) {

}
package prgrms.project.starbooks.controller.product;

import prgrms.project.starbooks.domain.product.Category;
import prgrms.project.starbooks.domain.product.Product;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

public record ProductRequest(
        String productName,
        Category category,
        long price,
        String description
) {

    public Product requestToProduct() {
        return Product.builder()
                .productId(randomUUID())
                .productName(this.productName())
                .category(this.category())
                .price(this.price())
                .description(this.description())
                .createdAt(now())
                .updatedAt(now())
                .build();
    }

}

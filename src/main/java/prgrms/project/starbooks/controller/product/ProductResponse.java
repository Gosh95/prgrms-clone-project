package prgrms.project.starbooks.controller.product;

import lombok.Builder;
import prgrms.project.starbooks.domain.product.Category;
import prgrms.project.starbooks.domain.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse (
        UUID productId,
        String productName,
        Category category,
        long price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @Builder
    public ProductResponse {
    }

    public static ProductResponse productToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .category(product.getCategory())
                .price(product.getPrice())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}

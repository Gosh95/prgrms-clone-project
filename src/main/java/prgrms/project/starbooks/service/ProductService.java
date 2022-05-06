package prgrms.project.starbooks.service;

import prgrms.project.starbooks.controller.product.ProductRequest;
import prgrms.project.starbooks.controller.product.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse registerProduct(ProductRequest productRequest);

    ProductResponse searchById(UUID productId);

    List<ProductResponse> searchAllProducts();

    ProductResponse updateProduct(UUID productId, ProductRequest productRequest);

    void deleteById(UUID productId);

    void deleteAllProducts();
}

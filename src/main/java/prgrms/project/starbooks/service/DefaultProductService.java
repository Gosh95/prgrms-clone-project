package prgrms.project.starbooks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prgrms.project.starbooks.controller.product.ProductRequest;
import prgrms.project.starbooks.controller.product.ProductResponse;
import prgrms.project.starbooks.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static prgrms.project.starbooks.controller.product.ProductResponse.productToResponse;
import static prgrms.project.starbooks.util.exception.ErrorMessage.CANT_FIND_PRODUCT;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse registerProduct(ProductRequest productRequest) {
        var product = productRequest.requestToProduct();
        var savedProduct = productRepository.save(product);

        return productToResponse(savedProduct);
    }

    @Override
    public ProductResponse searchById(UUID productId) {
        var retrievedProduct = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_PRODUCT.getMessage()));

        return productToResponse(retrievedProduct);
    }

    @Override
    public List<ProductResponse> searchAllProducts() {
        return productRepository.findAll().stream().map(ProductResponse::productToResponse).toList();
    }

    @Override
    public ProductResponse updateProduct(UUID productId, ProductRequest productRequest) {
        var product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException(CANT_FIND_PRODUCT.getMessage()));

        var updatedProduct = product.update(
                productRequest.productName(),
                productRequest.category(),
                productRequest.price(),
                productRequest.description()
        );

        var retrievedProduct = productRepository.update(updatedProduct);

        return productToResponse(retrievedProduct);
    }

    @Override
    public void deleteById(UUID productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}

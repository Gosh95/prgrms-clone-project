package prgrms.project.starbooks.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prgrms.project.starbooks.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> registerProduct(@RequestBody ProductRequest productRequest) {
        var productResponse = productService.registerProduct(productRequest);

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{productId}/products")
    public ResponseEntity<ProductResponse> searchById(@PathVariable UUID productId) {
        var productResponse = productService.searchById(productId);

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> searchAllProducts() {
        var productResponses = productService.searchAllProducts();

        return ResponseEntity.ok(productResponses);
    }

    @PatchMapping("/{productId}/products")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequest productRequest) {
        var productResponse = productService.updateProduct(productId, productRequest);

        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{productId}/products")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID productId) {
        productService.deleteById(productId);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/products")
    public ResponseEntity<Boolean> deleteAllProducts() {
        productService.deleteAllProducts();

        return ResponseEntity.ok(true);
    }

}

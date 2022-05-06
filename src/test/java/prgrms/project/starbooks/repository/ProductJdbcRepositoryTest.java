package prgrms.project.starbooks.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import prgrms.project.starbooks.config.TestConfig;
import prgrms.project.starbooks.domain.product.Product;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.project.starbooks.domain.product.Category.*;

class ProductJdbcRepositoryTest extends TestConfig {

    @Autowired
    ProductRepository productRepository;

    Product newProduct = new Product(randomUUID(), "test-product", IT, 1000L);

    static List<Product> getProducts() {
        return List.of(
                new Product(randomUUID(), "test-product1", IT, 1000L),
                new Product(randomUUID(), "test-product2", FICTION, 2000L),
                new Product(randomUUID(), "test-product3", ECONOMY, 3000L),
                new Product(randomUUID(), "test-product4", IT, 4000L),
                new Product(randomUUID(), "test-product5", FICTION, 5000L)
        );
    }

    @BeforeEach
    void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Product 를 저장할 수 있다.")
    void testSave() {
        var savedProduct = productRepository.save(newProduct);

        assertThat(savedProduct, notNullValue());
        assertThat(savedProduct, is(newProduct));
    }

    @Test
    @DisplayName("중복된 Product 는 예외가 발생해야 한다.")
    void testDuplicatedSave() {
        productRepository.save(newProduct);

        assertThrows(DataAccessException.class, () -> productRepository.save(newProduct));
    }

    @Test
    @DisplayName("ProductId 로 Product 를 찾을 수 있다.")
    void testFindById() {
        var savedProduct = productRepository.save(newProduct);
        var retrievedProduct = productRepository.findById(savedProduct.getProductId());

        assertThat(retrievedProduct.isEmpty(), is(false));
        assertThat(retrievedProduct.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @DisplayName("모든 Product 를 찾을 수 있다.")
    void testFindAll() {
        var products = getProducts();
        products.forEach(productRepository::save);
        var retrievedProducts = productRepository.findAll();

        assertThat(retrievedProducts.size(), is(5));
    }

    @Test
    @DisplayName("Product 를 업데이트할 수 있다.")
    void testUpdate() {
        var newProductName = "updated-product";
        var newPrice = 2000L;
        var newDescription = "updated-description";
        var beforeUpdate = newProduct.getUpdatedAt();

        var savedProduct = productRepository.save(newProduct);
        var updatedProduct = savedProduct.update(newProductName, FICTION, newPrice, newDescription);
        var retrievedProduct = productRepository.update(updatedProduct);

        assertThat(retrievedProduct, notNullValue());
        assertThat(retrievedProduct.getProductName(), is(newProductName));
        assertThat(retrievedProduct.getPrice(), is(newPrice));
        assertThat(retrievedProduct.getDescription(), is(newDescription));
        assertThat(retrievedProduct.getUpdatedAt(), greaterThan(beforeUpdate));
    }

    @Test
    @DisplayName("Product 를 삭제할 수 있다.")
    void testDeleteById() {
        var savedProduct = productRepository.save(newProduct);
        var beforeDelete = productRepository.findById(savedProduct.getProductId());

        assertThat(beforeDelete.isEmpty(), is(false));

        productRepository.deleteById(savedProduct.getProductId());
        var afterDelete = productRepository.findById(savedProduct.getProductId());

        assertThat(afterDelete.isEmpty(), is(true));
    }

    @Test
    @DisplayName("모든 Product 를 삭제할 수 있다.")
    void testDeleteAll() {
        var products = getProducts();
        products.forEach(productRepository::save);

        assertThat(productRepository.findAll().size(), is(5));

        productRepository.deleteAll();

        assertThat(productRepository.findAll().size(), is(0));
    }
}
package prgrms.project.starbooks.repository;

import prgrms.project.starbooks.domain.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID productId);

    List<Product> findAll();

    Product update(Product product);

    void deleteById(UUID productId);

    void deleteAll();
}

package prgrms.project.starbooks.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import prgrms.project.starbooks.domain.product.Category;
import prgrms.project.starbooks.domain.product.Product;
import prgrms.project.starbooks.util.exception.DataUnchangedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static prgrms.project.starbooks.util.MyObjectMapper.convertToMap;
import static prgrms.project.starbooks.util.Utils.toLocalDateTime;
import static prgrms.project.starbooks.util.Utils.toUUID;
import static prgrms.project.starbooks.util.exception.ErrorMessage.NOTHING_INSERTED;
import static prgrms.project.starbooks.util.exception.ErrorMessage.NOTHING_UPDATED;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductJdbcRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Product save(Product product) {
        var saved = jdbcTemplate.update(
                """
                        INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)
                         VALUES(UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)
                        """,
                toProductParamMap(product)
        );

        if (saved != 1) {
            log.error("Got different result than expected: {}", saved);

            throw new DataUnchangedException(NOTHING_INSERTED.getMessage());
        }

        return product;
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                            singletonMap("productId", productId.toString().getBytes()),
                            productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            log.error("Got empty result: {}", e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products ORDER BY created_at DESC", productRowMapper);
    }

    @Override
    public Product update(Product product) {
        var updated = jdbcTemplate.update(
                """
                        UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, updated_at = :updatedAt
                         WHERE product_id = UUID_TO_BIN(:productId)
                        """,
                toProductParamMap(product)
        );

        if (updated != 1) {
            log.error("Got different result than expected: {}", updated);

            throw new DataUnchangedException(NOTHING_UPDATED.getMessage());
        }

        return product;
    }

    @Override
    public void deleteById(UUID productId) {
        jdbcTemplate.update(
                "DELETE FROM products WHERE product_id = UUID_TO_BIN(:productId)",
                singletonMap("productId", productId.toString().getBytes())
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM products", emptyMap());
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var productName = resultSet.getString("product_name");
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var description = resultSet.getString("description");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));

        return Product.builder()
                .productId(productId)
                .productName(productName)
                .category(category)
                .price(price)
                .description(description)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    };

    private Map<String, Object> toProductParamMap(Product product) {
        return convertToMap(product);
    }
}

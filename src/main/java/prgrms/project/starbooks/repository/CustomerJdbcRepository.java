package prgrms.project.starbooks.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import prgrms.project.starbooks.domain.customer.Address;
import prgrms.project.starbooks.domain.customer.Customer;
import prgrms.project.starbooks.domain.customer.Email;
import prgrms.project.starbooks.domain.customer.Wallet;
import prgrms.project.starbooks.util.exception.DataUnchangedException;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static prgrms.project.starbooks.util.Utils.toLocalDateTime;
import static prgrms.project.starbooks.util.Utils.toUUID;
import static prgrms.project.starbooks.util.exception.ErrorMessage.NOTHING_INSERTED;
import static prgrms.project.starbooks.util.exception.ErrorMessage.NOTHING_UPDATED;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerJdbcRepository implements CustomerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Customer save(Customer customer) {
        var saved = jdbcTemplate.update(
                """
                        INSERT INTO customers(customer_id, customer_name, email, city, zipcode, money, created_at, updated_at)
                         VALUES(UUID_TO_BIN(:customerId), :customerName, :email, :city, :zipcode, :money, :createdAt, :updatedAt)
                        """,
                toCustomerParamMap(customer)
        );

        if (saved != 1) {
            log.error("Got different result than expected: {}", saved);

            throw new DataUnchangedException(NOTHING_INSERTED.getMessage());
        }

        return customer;
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM customers WHERE customer_id = UUID_TO_BIN(:customerId)",
                    singletonMap("customerId", customerId.toString().getBytes()),
                    customerRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            log.error("Got empty result: {}", e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("SELECT * FROM customers ORDER BY created_at DESC", customerRowMapper);
    }

    @Override
    public Customer update(Customer customer) {
        var updated = jdbcTemplate.update(
                """
                        UPDATE customers SET customer_name = :customerName, city = :city, zipcode = :zipcode, money = :money
                         WHERE customer_id = UUID_TO_BIN(:customerId)
                        """,
                toCustomerParamMap(customer)
        );

        if (updated != 1) {
            log.error("Got different result than expected: {}", updated);

            throw new DataUnchangedException(NOTHING_UPDATED.getMessage());
        }

        return customer;
    }

    @Override
    public void deleteById(UUID customerId) {
        jdbcTemplate.update(
                "DELETE FROM customers WHERE customer_id = UUID_TO_BIN(:customerId)",
                singletonMap("customerId", customerId.toString().getBytes())
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM customers", emptyMap());
    }

    private Map<String, Object> toCustomerParamMap(Customer customer) {
        var paramMap = new HashMap<String, Object>();

        paramMap.put("customerId", customer.getCustomerId().toString().getBytes());
        paramMap.put("customerName", customer.getCustomerName());
        paramMap.put("email", customer.getEmail().address());
        paramMap.put("city", customer.getAddress().city());
        paramMap.put("zipcode", customer.getAddress().zipcode());
        paramMap.put("money", customer.getWallet() == null ? 0L : customer.getWallet().getMoney());
        paramMap.put("createdAt", customer.getCreatedAt());
        paramMap.put("updatedAt", customer.getUpdatedAt());

        return paramMap;
    }

    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var customerName = resultSet.getString("customer_name");
        var email = new Email((resultSet.getString("email")));
        var city = resultSet.getString("city");
        var zipcode = resultSet.getString("zipcode");
        var money = resultSet.getLong("money");
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));

        return Customer.builder()
                .customerId(customerId)
                .customerName(customerName)
                .email(email)
                .address(new Address(city, zipcode))
                .wallet(new Wallet(money))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    };

}

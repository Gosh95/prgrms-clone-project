CREATE TABLE customers
(
    customer_id   binary(16) PRIMARY KEY,
    customer_name VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NOT NULL,
    city          VARCHAR(200) NOT NULL,
    zipcode       VARCHAR(6)   NOT NULL,
    money         BIGINT      DEFAULT 0,
    created_at    datetime(6)  NOT NULL,
    updated_at    datetime(6) DEFAULT NULL,
    constraint unq_customer_email unique (email)
);

CREATE TABLE products
(
    product_id   BINARY(16) PRIMARY KEY,
    product_name VARCHAR(50)  NOT NULL,
    category     VARCHAR(20)  NOT NULL,
    price        BIGINT       NOT NULL,
    description  VARCHAR(500) DEFAULT NULL,
    created_at   datetime(6)  NOT NULL,
    updated_at   datetime(6) DEFAULT NULL
);

CREATE TABLE orders
(
    order_id       binary(16) PRIMARY KEY,
    order_status   VARCHAR(50) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    created_at     datetime(6) NOT NULL,
    updated_at     datetime(6) DEFAULT NULL
);

CREATE TABLE order_items
(
    seq        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   binary(16)  NOT NULL,
    product_id binary(16)  NOT NULL,
    product_name VARCHAR(50)  NOT NULL,
    category   VARCHAR(30) NOT NULL,
    price      bigint      NOT NULL,
    quantity   int         NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE order_details
(
    seq           BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id      BINARY(16),
    customer_id   BINARY(16)   NOT NULL,
    customer_name VARCHAR(50)  NOT NULL,
    city          VARCHAR(200) NOT NULL,
    zipcode       VARCHAR(6)   NOT NULL,
    order_status  VARCHAR(50)  NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6) DEFAULT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_details_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_details_to_customer FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    order_number INT
);

CREATE TABLE line_items (
    id BIGINT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    variant_id BIGINT,
    sku VARCHAR(50),

    FOREIGN KEY (order_id)
        REFERENCES orders (id)
);

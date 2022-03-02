CREATE TABLE order_line
(
    id            BIGSERIAL    NOT NULL,
    order_id      BIGINT       NOT NULL,
    sku           VARCHAR(64)  NOT NULL,
    title         VARCHAR(255) NOT NULL,
    variant_title VARCHAR(255) NOT NULL,
    quantity      INT          NOT NULL,
    price         INT          NULL,

    CONSTRAINT order_line_pk
        PRIMARY KEY (id),

    CONSTRAINT order_line_order_id_fk
        FOREIGN KEY (order_id) REFERENCES "order" (id)
            ON DELETE CASCADE
);

CREATE UNIQUE INDEX order_line_id_uindex
    ON order_line (id);


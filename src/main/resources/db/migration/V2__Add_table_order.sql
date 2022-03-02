CREATE TABLE "order"
(
    id                   BIGSERIAL   NOT NULL,
    name                 VARCHAR(16) NOT NULL,
    number               INT         NOT NULL,
    status               VARCHAR(50) NOT NULL,
    cancel_reason        VARCHAR(10) NULL,
    financial_status     VARCHAR(20) NOT NULL,
    total_discount       INT         NULL,
    subtotal_price       INT         NULL,
    total_tax            INT         NULL,
    total_price          INT         NULL,
    total_shipping_price INT         NULL,
    created_date_time    TIMESTAMPTZ NOT NULL,
    valid_from           TIMESTAMPTZ NOT NULL,
    valid_to             TIMESTAMPTZ NULL,

    CONSTRAINT order_pk
        PRIMARY KEY (id),

    CONSTRAINT order_name_key
        UNIQUE (name, valid_from, valid_to)
);

CREATE UNIQUE INDEX order_id_uindex
    ON "order" (id);


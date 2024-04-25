CREATE TABLE IF NOT EXISTS products
(
    product_id      BIGINT      NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_name    TEXT        NOT NULL,
    amount          BIGINT      DEFAULT 0,
    image           TEXT        NOT NULL,

    PRIMARY KEY (product_id)
);
CREATE TABLE IF NOT EXISTS products
(
    product_id      BIGINT      NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_name    TEXT        NOT NULL,
    description     TEXT        NOT NULL,
    price           INT         NOT NULL,
    amount          BIGINT      DEFAULT 0,

    PRIMARY KEY (product_id)
);
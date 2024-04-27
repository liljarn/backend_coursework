CREATE TABLE IF NOT EXISTS products
(
    product_id      BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_name    VARCHAR(100)    NOT NULL,
    description     TEXT            NOT NULL,
    price           INT             NOT NULL,
    amount          BIGINT          DEFAULT 0,

    PRIMARY KEY (product_id)
);

CREATE TABLE IF NOT EXISTS customer
(
    customer_id         BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    customer_name       VARCHAR(20)     NOT NULL,
    customer_surname    VARCHAR(30)     NOT NULL,
    email               VARCHAR(255)    NOT NULL,
    password            TEXT            NOT NULL,
    role                VARCHAR(20),

    PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS favorite_products
(
    customer_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (customer_id)               REFERENCES customer(customer_id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id)                REFERENCES products(product_id)     ON DELETE CASCADE,
    PRIMARY KEY (customer_id, product_id)
);

CREATE TABLE IF NOT EXISTS cart_products
(
    customer_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (customer_id)               REFERENCES customer(customer_id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id)                REFERENCES products(product_id)     ON DELETE CASCADE,
    PRIMARY KEY (customer_id, product_id)
);
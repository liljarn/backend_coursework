CREATE TABLE IF NOT EXISTS products
(
    product_id      BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_name    VARCHAR(100)    NOT NULL,
    description     TEXT            NOT NULL,
    price           INT             NOT NULL,
    amount          BIGINT          DEFAULT 0,

    UNIQUE (product_name),
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

    UNIQUE (email),
    PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS cart
(
    cart_id     BIGINT      NOT NULL,
    PRIMARY KEY (cart_id)
);

CREATE TABLE IF NOT EXISTS cart_product
(
    cart_id         BIGINT      NOT NULL,
    product_id      BIGINT      NOT NULL,
    quantity        BIGINT      DEFAULT 1,

    FOREIGN KEY (cart_id)                   REFERENCES cart(cart_id)            ON DELETE CASCADE,
    FOREIGN KEY (product_id)                REFERENCES products(product_id)     ON DELETE CASCADE,
    PRIMARY KEY (cart_id, product_id)
);

CREATE TABLE IF NOT EXISTS favorite_products
(
    customer_id     BIGINT,
    product_id      BIGINT,

    FOREIGN KEY (customer_id)               REFERENCES customer(customer_id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id)                REFERENCES products(product_id)     ON DELETE CASCADE,
    PRIMARY KEY (customer_id, product_id)
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_id      BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    customer_id     BIGINT          NOT NULL,
    customer_name   VARCHAR(255)    NOT NULL,
    comment         TEXT            NOT NULL,
    rate            INT             NOT NULL,

    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    PRIMARY KEY (comment_id)
)
package edu.mirea.candy_shop.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product was not found");
    }
}

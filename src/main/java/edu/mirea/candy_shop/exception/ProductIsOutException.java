package edu.mirea.candy_shop.exception;

public class ProductIsOutException extends RuntimeException {
    public ProductIsOutException(String message) {
        super(message);
    }
}

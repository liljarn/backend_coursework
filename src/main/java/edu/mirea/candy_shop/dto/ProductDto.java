package edu.mirea.candy_shop.dto;

import java.net.URI;

public record ProductDto(Long productId, String productName, String description, int price, long amount, URI image) {
}

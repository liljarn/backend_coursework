package edu.mirea.candy_shop.dto;

import java.net.URI;

public record CartProductDto(Long productId, String productName, String description, long price, long quantity,
                             URI image) {
}

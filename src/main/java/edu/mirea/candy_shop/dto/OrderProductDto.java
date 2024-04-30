package edu.mirea.candy_shop.dto;

import java.net.URI;

public record OrderProductDto(String name, long price, long amount, URI image) {
}

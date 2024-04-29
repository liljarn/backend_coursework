package edu.mirea.candy_shop.dto;

import java.net.URI;

public record CommentDto(String name, String comment, URI image, int rate) {
}

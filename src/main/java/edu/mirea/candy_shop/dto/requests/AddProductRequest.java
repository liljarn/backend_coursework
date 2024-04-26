package edu.mirea.candy_shop.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public record AddProductRequest(String productName, String description, int price, long amount, MultipartFile image) {
}

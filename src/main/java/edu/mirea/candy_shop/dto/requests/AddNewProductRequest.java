package edu.mirea.candy_shop.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public record AddNewProductRequest(String productName, String description, int price, long amount, MultipartFile image) {
}

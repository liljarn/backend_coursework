package edu.mirea.candy_shop.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public record AddCommentRequest(String customerName, String comment, int rate, MultipartFile image) {
}

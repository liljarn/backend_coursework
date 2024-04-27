package edu.mirea.candy_shop.dto.requests;

public record SignUpRequest(String customerName, String customerSurname, String email, String password) {
}

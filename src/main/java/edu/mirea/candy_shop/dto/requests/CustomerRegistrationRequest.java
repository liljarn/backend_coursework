package edu.mirea.candy_shop.dto.requests;

public record CustomerRegistrationRequest(String customerName, String customerSurname, String email, String password) {
}

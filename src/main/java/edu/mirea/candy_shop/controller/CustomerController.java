package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.CustomerData;
import edu.mirea.candy_shop.service.CustomerService;
import edu.mirea.candy_shop.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final JwtService jwtService;

    private static final String TOKEN = "Token";

    @GetMapping
    public CustomerData getCustomerData(@RequestHeader(TOKEN) String token) {
        String email = jwtService.extractUserName(token);
        return customerService.getCustomer(email);
    }
}

package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.OrderDto;
import edu.mirea.candy_shop.service.JwtService;
import edu.mirea.candy_shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final JwtService jwtService;
    private final OrderService orderService;

    @PostMapping
    public void makeOrder(@RequestHeader("Token") String token) {
        String email = jwtService.extractUserName(token);
        orderService.makeOrder(email);
    }

    @GetMapping
    public List<OrderDto> getCustomerOrders(@RequestHeader("Token") String token) {
        String email = jwtService.extractUserName(token);
        return orderService.getOrders(email);
    }
}

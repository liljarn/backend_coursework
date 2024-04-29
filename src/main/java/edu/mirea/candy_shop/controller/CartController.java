package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dto.CartProductDto;
import edu.mirea.candy_shop.dto.requests.ProductRequest;
import edu.mirea.candy_shop.dto.response.InCartResponse;
import edu.mirea.candy_shop.service.CartService;
import edu.mirea.candy_shop.service.JwtService;
import edu.mirea.candy_shop.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final PictureService pictureService;
    private final JwtService jwtService;

    private static final String TOKEN = "Token";

    @PostMapping
    public void addToCart(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        cartService.addCartProduct(email, request.productId());
    }

    @DeleteMapping
    public void removeFromCart(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        cartService.removeCartProduct(email, request.productId());
    }

    @GetMapping("/{id}")
    public InCartResponse inCart(@RequestHeader(TOKEN) String token, @PathVariable Long id) {
        String email = jwtService.extractUserName(token);
        return new InCartResponse(cartService.isInCart(email, id));
    }

    @GetMapping
    public List<CartProductDto> getCart(@RequestHeader(TOKEN) String token) {
        String email = jwtService.extractUserName(token);
        return cartService.getCart(email).stream().map(entity -> {
            ProductEntity product = entity.getProduct();
            return new CartProductDto(
                    product.getProductId(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice() * entity.getQuantity(),
                    entity.getQuantity(),
                    URI.create(pictureService.getLinkOnPicture(product.getProductName()))
            );
        }).toList();
    }

}

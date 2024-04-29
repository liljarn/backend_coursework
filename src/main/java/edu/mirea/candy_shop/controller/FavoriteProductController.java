package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.ProductDto;
import edu.mirea.candy_shop.dto.requests.ProductRequest;
import edu.mirea.candy_shop.dto.response.IsFavoriteResponse;
import edu.mirea.candy_shop.service.FavoriteProductService;
import edu.mirea.candy_shop.service.JwtService;
import edu.mirea.candy_shop.service.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fav")
public class FavoriteProductController {
    private final FavoriteProductService favoriteProductService;
    private final PictureService pictureService;
    private final JwtService jwtService;

    private static final String TOKEN = "Token";

    @GetMapping
    @SneakyThrows
    public List<ProductDto> getFavorite(@RequestHeader(TOKEN) String token) {
        String email = jwtService.extractUserName(token);
        return favoriteProductService.getFavoriteProducts(email).stream().map(entity ->
                new ProductDto(
                        entity.getProductId(),
                        entity.getProductName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getAmount(),
                        URI.create(pictureService.getLinkOnPicture(entity.getProductName()))
                )
        ).toList();
    }

    @GetMapping("/{id}")
    public IsFavoriteResponse isFavorite(@RequestHeader(TOKEN) String token, @PathVariable Long id) {
        String email = jwtService.extractUserName(token);
        return new IsFavoriteResponse(favoriteProductService.isProductFavorite(email, id));
    }

    @PostMapping
    public void addToFavorite(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        favoriteProductService.addFavouriteProduct(email, request.productId());
    }

    @DeleteMapping
    public void removeFromFavorite(@RequestHeader(TOKEN) String token, @RequestBody ProductRequest request) {
        String email = jwtService.extractUserName(token);
        favoriteProductService.removeFavoriteProduct(email, request.productId());
    }
}

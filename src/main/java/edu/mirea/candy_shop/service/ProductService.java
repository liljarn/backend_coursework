package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CartEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.dto.requests.AddNewProductRequest;
import edu.mirea.candy_shop.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<ProductEntity> getProducts() {
        return productRepository.findAll().stream().filter(entity -> !entity.isRemoved()).toList();
    }

    @Transactional
    public void addProduct(AddNewProductRequest request) {
        ProductEntity product =
                new ProductEntity(request.productName(), request.description(), request.price(), request.amount());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        List<CustomerEntity> customers = customerRepository.findAll();
        customers.forEach(customer -> {
            customer.setFavoriteProducts(
                    customer.getFavoriteProducts()
                            .stream()
                            .filter(productInFav -> !Objects.equals(productInFav.getProductId(), productId))
                            .collect(Collectors.toSet())
            );
            CartEntity cart = customer.getCart();
            cart.setProductsInCart(
                    cart.getProductsInCart()
                    .stream()
                    .filter(productInCart -> !Objects.equals(productInCart.getId().getProductId(), productId))
                    .collect(Collectors.toSet())
            );
        });
        product.setRemoved(true);
    }
}

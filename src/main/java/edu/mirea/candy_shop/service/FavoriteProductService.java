package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Set<ProductEntity> getFavoriteProducts(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return customer.getFavoriteProducts();
    }

    @Transactional(readOnly = true)
    public boolean isProductFavorite(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        return customer.getFavoriteProducts().contains(product);
    }

    @Transactional
    public void addFavouriteProduct(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        customer.addProductToFavorite(product);
    }

    @Transactional
    public void removeFavoriteProduct(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        customer.getFavoriteProducts().remove(product);
    }
}

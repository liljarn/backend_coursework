package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.dto.requests.AddNewProductRequest;
import edu.mirea.candy_shop.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void addProduct(AddNewProductRequest request) {
        ProductEntity product =
                new ProductEntity(request.productName(), request.description(), request.price(), request.amount());
        productRepository.save(product);
    }

    @Transactional
    public String deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        String name = product.getProductName();
        productRepository.delete(product);
        return name;
    }
}

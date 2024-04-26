package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.dto.requests.AddProductRequest;
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
    public void addProduct(AddProductRequest request) {
        ProductEntity product =
                new ProductEntity(request.productName(), request.description(), request.price(), request.amount());
        productRepository.save(product);
    }
}

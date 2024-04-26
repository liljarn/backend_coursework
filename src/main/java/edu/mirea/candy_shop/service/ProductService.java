package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }
}

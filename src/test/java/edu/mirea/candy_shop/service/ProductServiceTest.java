package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.dto.requests.AddNewProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {
    @Test
    @DisplayName("ProductService#getProducts test")
    public void getProducts_shouldReturnProductsList() {
        // Arrange
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        Mockito.when(productRepository.findAll(Sort.by(Sort.Direction.ASC, "productId")))
                .thenReturn(Collections.emptyList());
        ProductService productService = new ProductService(
                productRepository,
                Mockito.mock(CustomerRepository.class),
                Mockito.mock(FavoriteProductService.class),
                Mockito.mock(CartService.class)
        );

        // Act
        List<ProductEntity> products = productService.getProducts();

        // Assert
        assertThat(products).isEmpty();
        Mockito.verify(productRepository).findAll(Sort.by(Sort.Direction.ASC, "productId"));
    }

    @Test
    @DisplayName("ProductService#addProduct test")
    public void addProduct_shouldCorrectlyAddNewProduct() {
        // Arrange
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        ProductService productService = new ProductService(
                productRepository,
                Mockito.mock(CustomerRepository.class),
                Mockito.mock(FavoriteProductService.class),
                Mockito.mock(CartService.class)
        );

        // Act
        productService.addProduct(new AddNewProductRequest(
                "test",
                "this is test",
                100,
                100,
                new MockMultipartFile("test", new byte[0])
        ));

        // Assert
        Mockito.verify(productRepository).save(Mockito.any(ProductEntity.class));
    }
}

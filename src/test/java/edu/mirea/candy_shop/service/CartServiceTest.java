package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CartEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.entity.ProductEntity;
import edu.mirea.candy_shop.dao.entity.link_tables.CartProductEntity;
import edu.mirea.candy_shop.dao.entity.link_tables.CartProductId;
import edu.mirea.candy_shop.dao.repository.CartProductRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.dto.CartProductDto;
import edu.mirea.candy_shop.exception.ProductIsOutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartProductRepository cartProductRepository;

    @Mock
    private PictureService pictureService;

    private CustomerEntity customer;
    private CartEntity cart;
    private ProductEntity product;
    private CartProductEntity cartProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new CustomerEntity();
        customer.setEmail("test@example.com");

        cart = new CartEntity();
        cart.setCartId(1L);
        customer.setCart(cart);
        cart.setCustomer(customer);

        product = new ProductEntity();
        product.setProductId(1L);
        product.setProductName("Product");
        product.setPrice(10);
        product.setAmount(5);

        cartProduct = new CartProductEntity(new CartProductId(1L, 1L));
        cartProduct.setProduct(product);
        cartProduct.setQuantity(2L);
        cartProduct.setCart(cart);
    }

    @Test
    void addCartProduct_SuccessfullyAdded() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartProductRepository.findById(any())).thenReturn(Optional.empty());
        cartService.addCartProduct("test@example.com", 1L);
        assertThat(cart.getProductsInCart()).hasSize(1);
    }

    @Test
    void addCartProduct_ProductIsOutOfStock_ThrowsException() {
        product.setAmount(0);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThatThrownBy(() -> cartService.addCartProduct("test@example.com", 1L))
                .isInstanceOf(ProductIsOutException.class);
    }

    @Test
    void removeCartProduct_SuccessfullyRemoved() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartProductRepository.findById(any())).thenReturn(Optional.of(cartProduct));
        cartService.removeCartProduct("test@example.com", 1L);
        assertThat(cart.getProductsInCart()).isEmpty();
    }

    @Test
    void removeFromCart_SuccessfullyRemoved() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartProductRepository.findById(any())).thenReturn(Optional.of(cartProduct));
        cartService.removeFromCart("test@example.com", 1L);
        assertThat(cart.getProductsInCart()).isEmpty();
    }

    @Test
    void isInCart_ReturnsQuantity() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartProductRepository.findById(any())).thenReturn(Optional.of(cartProduct));
        assertThat(cartService.isInCart("test@example.com", 1L)).isEqualTo(2L);
    }

    @Test
    void getCart_ReturnsCartProducts() {
        Set<CartProductEntity> cartProducts = new HashSet<>();
        cartProducts.add(cartProduct);
        cart.setProductsInCart(cartProducts);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(pictureService.getLinkOnPicture(any())).thenReturn("dummy");
        List<CartProductDto> expectedCartProducts = new ArrayList<>();
        expectedCartProducts.add(new CartProductDto(1L, "Product", 10, 2L, URI.create("dummy")));
        List<CartProductDto> actualCartProducts = cartService.getCart("test@example.com");
        assertThat(actualCartProducts).isEqualTo(expectedCartProducts);
    }
}


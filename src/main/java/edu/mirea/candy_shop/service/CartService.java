package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.*;
import edu.mirea.candy_shop.dao.repository.CartProductRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dao.repository.ProductRepository;
import edu.mirea.candy_shop.exception.CustomerNotFoundException;
import edu.mirea.candy_shop.exception.ProductIsOutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void addCartProduct(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        if (product.getAmount() == 0) {
            throw new ProductIsOutException("Product is out of stock");
        }
        CartEntity cart = customer.getCart();
        CartProductId cartProductId = new CartProductId(cart.getCartId(), productId);
        Optional<CartProductEntity> cartProduct = cartProductRepository.findById(cartProductId);
//        Optional<CartProductEntity> cartProduct = cart.getProductsInCart().stream()
//                .filter(cartProductEntity -> cartProductEntity.getProduct().equals(product))
//                .findFirst();
        if (cartProduct.isEmpty()) {
            CartProductEntity cartProductEntity = addNewProduct(cart, product);
            cartProductEntity.setQuantity(cartProductEntity.getQuantity() + 1);
            product.setAmount(product.getAmount() - 1);
        } else {
            cartProduct.get().setQuantity(cartProduct.get().getQuantity() + 1);
            product.setAmount(product.getAmount() - 1);
        }
    }

    @Transactional
    public void removeCartProduct(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        CartEntity cart = customer.getCart();
        CartProductEntity cartProduct = cartProductRepository.findById(new CartProductId(cart.getCartId(), productId))
                .orElseThrow(RuntimeException::new);
        if (cartProduct.getQuantity().equals(1L)) {
            cartProductRepository.delete(cartProduct);
        } else {
            cartProduct.setQuantity(cartProduct.getQuantity() - 1);
        }
        product.setAmount(product.getAmount() + 1);
    }

    @Transactional
    public void removeFromCart(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        CartEntity cart = customer.getCart();
        CartProductEntity cartProduct = cartProductRepository.findById(new CartProductId(cart.getCartId(), productId))
                .orElseThrow(RuntimeException::new);
        product.setAmount(product.getAmount() + cartProduct.getQuantity());
        product.getCarts().remove(cartProduct);
        cart.getProductsInCart().remove(cartProduct);
        cartProductRepository.delete(cartProduct);
    }

    @Transactional(readOnly = true)
    public Long isInCart(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CartEntity cart = customer.getCart();
        Optional<CartProductEntity> cartProduct = cartProductRepository
                .findById(new CartProductId(cart.getCartId(), productId));
        if (cartProduct.isPresent()) {
            return cartProduct.get().getQuantity();
        }
        return 0L;
    }

    @Transactional
    public Set<CartProductEntity> getCart(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CartEntity cart = customer.getCart();
        return cart.getProductsInCart();
    }

    private CartProductEntity addNewProduct(CartEntity cart, ProductEntity product) {
        CartProductId id = new CartProductId(cart.getCartId(), product.getProductId());
        CartProductEntity cartProduct = new CartProductEntity(id);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cart.getProductsInCart().add(cartProduct);
        product.getCarts().add(cartProduct);
        return cartProduct;
    }
}
